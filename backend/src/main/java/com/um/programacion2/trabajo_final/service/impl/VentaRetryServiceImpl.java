package com.um.programacion2.trabajo_final.service.impl;

import com.um.programacion2.trabajo_final.config.ApplicationProperties;
import com.um.programacion2.trabajo_final.domain.AsientoVendido;
import com.um.programacion2.trabajo_final.domain.Venta;
import com.um.programacion2.trabajo_final.enumeration.EstadoVenta;
import com.um.programacion2.trabajo_final.repository.VentaRepository;
import com.um.programacion2.trabajo_final.service.dto.VentaDTO;
import com.um.programacion2.trabajo_final.service.dto.catedra.AsientoVentaCatedraDTO;
import com.um.programacion2.trabajo_final.service.dto.catedra.VentaCatedraRequest;
import com.um.programacion2.trabajo_final.service.dto.catedra.VentaCatedraResponse;
import com.um.programacion2.trabajo_final.service.mapper.VentaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class VentaRetryServiceImpl {

    private final Logger log = LoggerFactory.getLogger(VentaRetryServiceImpl.class);

    private final String catedraVentaUrl;
    private final VentaMapper ventaMapper;
    private final RestTemplate restTemplate;
    private final VentaRepository ventaRepository;

    public VentaRetryServiceImpl(
        VentaMapper ventaMapper,
        RestTemplate restTemplate,
        VentaRepository ventaRepository,
        ApplicationProperties applicationProperties
    ) {
        this.ventaMapper = ventaMapper;
        this.restTemplate = restTemplate;
        this.ventaRepository = ventaRepository;
        this.catedraVentaUrl = applicationProperties.getCatedra().getVentaUrl();
    }

    public VentaDTO reconciliarVentaManual(Long ventaId) {
        log.info("MANUAL: Solicitud de reintento para Venta ID: {}", ventaId);

        Venta venta = ventaRepository.findById(ventaId)
            .orElseThrow(() -> new RuntimeException("Venta no encontrada: " + ventaId));

        if (EstadoVenta.CONFIRMADA.equals(venta.getEstadoVenta())) {
            return ventaMapper.toDto(venta);
        }

        if (!EstadoVenta.PENDIENTE.equals(venta.getEstadoVenta()) && !EstadoVenta.RECHAZADA.equals(venta.getEstadoVenta())) {
            throw new RuntimeException("No se puede reintentar la venta. Estado actual inválido: " + venta.getEstadoVenta());
        }

        ejecutarReintento(venta);

        return ventaMapper.toDto(venta);
    }


    private void ejecutarReintento(Venta venta) {
        log.debug("Intentando conectar con Cátedra para venta {}", venta.getId());

        if (venta.getEvento().getEventoIdCatedra() == null) {
            log.error("No se puede reintentar la venta {}: El evento no tiene ID de Cátedra.", venta.getId());
            venta.setDescripcion("Fallo reintento: Evento no sincronizado (ID Cátedra null).");
            ventaRepository.save(venta);
            return;
        }

        VentaCatedraRequest request = construirRequest(venta);

        try {

            VentaCatedraResponse response = enviarSolicitud(request);

            if (response != null) {
                actualizarEstadoVenta(venta, response);
                ventaRepository.save(venta);
            }
        } catch (Exception e) {
            log.warn("FALLO RED: No se pudo completar reintento para venta {}. Sigue PENDIENTE.", venta.getId());
            throw new RuntimeException("Error de comunicación con Cátedra: " + e.getMessage());
        }
    }

    private VentaCatedraRequest construirRequest(Venta venta){
        VentaCatedraRequest request = new VentaCatedraRequest();
        request.setEventoId(venta.getEvento().getEventoIdCatedra());
        request.setFecha(Instant.now());
        request.setPrecioVenta(venta.getPrecioVenta());

        List<AsientoVentaCatedraDTO> asientosDTO = new ArrayList<>();
        for (AsientoVendido av : venta.getAsientos()) { //
            asientosDTO.add(new AsientoVentaCatedraDTO(av.getFila(), av.getColumna(), av.getPersona()));
        }
        request.setAsientos(asientosDTO);
        return request;
    }

    private VentaCatedraResponse enviarSolicitud(VentaCatedraRequest request) {
        log.debug("Enviando POST a Cátedra: {}", catedraVentaUrl);
        return restTemplate.postForObject(catedraVentaUrl, request, VentaCatedraResponse.class);
    }

    private void actualizarEstadoVenta(Venta venta, VentaCatedraResponse response) {
        venta.setFechaVenta(response.getFechaVenta());
        venta.setResultado(response.getResultado());

        if (Boolean.TRUE.equals(response.getResultado())) {
            venta.setEstadoVenta(EstadoVenta.CONFIRMADA);
            venta.setVentaIdCatedra(response.getVentaId());
            venta.setDescripcion("Confirmada (Reintento Manual)");
            log.info("EXITO: Venta {} recuperada y CONFIRMADA.", venta.getId());
        } else {
            venta.setEstadoVenta(EstadoVenta.RECHAZADA);
            venta.setDescripcion(response.getDescripcion());
            log.warn("RECHAZADA: Venta {} rechazada por Cátedra. Motivo: {}", venta.getId(), response.getDescripcion());
        }
    }

}
