package com.um.programacion2.trabajo_final.service.impl; // Ojo con el package, ajustalo si es necesario

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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class VentaRetryService {

    private final Logger log = LoggerFactory.getLogger(VentaRetryService.class);

    private final VentaRepository ventaRepository;
    private final RestTemplate restTemplate;
    private final VentaMapper ventaMapper;
    private final String catedraVentaUrl;

    public VentaRetryService(VentaRepository ventaRepository,
                             RestTemplate restTemplate,
                             VentaMapper ventaMapper,
                             ApplicationProperties applicationProperties) {
        this.ventaRepository = ventaRepository;
        this.restTemplate = restTemplate;
        this.ventaMapper = ventaMapper;
        this.catedraVentaUrl = applicationProperties.getCatedra().getVentaUrl();
    }

    /**
     * AUTOMÁTICO: Se ejecuta cada 60 segundos.
     */
    @Scheduled(fixedDelay = 60000)
    public void procesarVentasPendientes() {
        // Buscamos PENDIENTE
        List<Venta> ventasPendientes = ventaRepository.findAllByEstadoVenta(EstadoVenta.PENDIENTE);

        if (!ventasPendientes.isEmpty()) {
            log.info("CRON: Se encontraron {} ventas pendientes. Procesando...", ventasPendientes.size());
            for (Venta venta : ventasPendientes) {
                try {
                    // Reutilizamos la lógica central
                    ejecutarReintento(venta);
                } catch (Exception e) {
                    log.error("CRON: Error al reintentar venta {}: {}", venta.getId(), e.getMessage());
                }
            }
        }
    }

    /**
     * MANUAL: Llamado desde el Controlador.
     */
    public VentaDTO reconciliarVentaManual(Long ventaId) {
        log.info("MANUAL: Solicitud de reintento para Venta ID: {}", ventaId);

        Venta venta = ventaRepository.findById(ventaId)
            .orElseThrow(() -> new RuntimeException("Venta no encontrada: " + ventaId));

        // Validación extra para no reintentar lo que ya está confirmado
        if (EstadoVenta.CONFIRMADA.equals(venta.getEstadoVenta())) {
            return ventaMapper.toDto(venta);
        }

        // Ejecutamos la misma lógica que el Cron
        ejecutarReintento(venta);

        return ventaMapper.toDto(venta);
    }

    /**
     * LÓGICA CENTRAL: Contiene el código de comunicación con la Cátedra.
     * Es privado porque solo se usa internamente por los dos métodos anteriores.
     */
    private void ejecutarReintento(Venta venta) {
        log.debug("Intentando conectar con Cátedra para venta {}", venta.getId());

        if (venta.getEvento().getEventoIdCatedra() == null) {
            log.error("No se puede reintentar la venta {}: El evento no tiene ID de Cátedra.", venta.getId());
            venta.setDescripcion("Fallo reintento: Evento no sincronizado (ID Cátedra null).");
            ventaRepository.save(venta);
            return; // Abortar
        }

        // 1. Construir Request
        VentaCatedraRequest request = new VentaCatedraRequest();
        request.setEventoId(venta.getEvento().getEventoIdCatedra()); //
        request.setFecha(Instant.now());
        request.setPrecioVenta(venta.getPrecioVenta());

        List<AsientoVentaCatedraDTO> asientosDTO = new ArrayList<>();
        // Usamos getAsientos() porque la entidad Venta tiene el Set<AsientoVendido>
        for (AsientoVendido av : venta.getAsientos()) { //
            asientosDTO.add(new AsientoVentaCatedraDTO(av.getFila(), av.getColumna(), av.getPersona()));
        }
        request.setAsientos(asientosDTO);

        // 2. Llamada API
        try {
            VentaCatedraResponse response = restTemplate.postForObject(catedraVentaUrl, request, VentaCatedraResponse.class);

            if (response != null) {
                // Actualizamos datos
                venta.setFechaVenta(response.getFechaVenta());
                venta.setResultado(response.getResultado());

                if (Boolean.TRUE.equals(response.getResultado())) {
                    venta.setEstadoVenta(EstadoVenta.CONFIRMADA);
                    venta.setVentaIdCatedra(response.getVentaId());
                    venta.setDescripcion("Confirmada (Reintento)");
                    log.info("EXITO: Venta {} confirmada.", venta.getId());
                } else {
                    venta.setEstadoVenta(EstadoVenta.RECHAZADA);
                    venta.setDescripcion(response.getDescripcion());
                    log.warn("RECHAZADA: Venta {}. Causa: {}", venta.getId(), response.getDescripcion());
                }
                ventaRepository.save(venta);
            }
        } catch (Exception e) {
            log.warn("FALLO RED: No se pudo completar reintento para venta {}. Sigue PENDIENTE.", venta.getId());
            throw new RuntimeException("Error de comunicación con Cátedra: " + e.getMessage());
        }
    }
}
