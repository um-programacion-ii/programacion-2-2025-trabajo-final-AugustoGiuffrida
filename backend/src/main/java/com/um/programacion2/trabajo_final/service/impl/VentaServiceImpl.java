package com.um.programacion2.trabajo_final.service.impl;

import com.um.programacion2.trabajo_final.config.ApplicationProperties;
import com.um.programacion2.trabajo_final.domain.AsientoVendido;
import com.um.programacion2.trabajo_final.domain.Evento;
import com.um.programacion2.trabajo_final.domain.User;
import com.um.programacion2.trabajo_final.domain.Venta;
import com.um.programacion2.trabajo_final.enumeration.EstadoVenta;
import com.um.programacion2.trabajo_final.enumeration.EstadoSesion;
import com.um.programacion2.trabajo_final.repository.AsientoVendidoRepository;
import com.um.programacion2.trabajo_final.repository.EventoRepository;
import com.um.programacion2.trabajo_final.repository.UserRepository;
import com.um.programacion2.trabajo_final.repository.VentaRepository;
import com.um.programacion2.trabajo_final.service.SesionService;
import com.um.programacion2.trabajo_final.service.VentaService;
import com.um.programacion2.trabajo_final.service.dto.*;
import com.um.programacion2.trabajo_final.service.dto.catedra.*;
import com.um.programacion2.trabajo_final.service.mapper.VentaMapper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.time.Instant;

/**
 * Service Implementation for managing {@link com.um.programacion2.trabajo_final.domain.Venta}.
 */
@Service
@Transactional
public class VentaServiceImpl implements VentaService {

    private static final Logger LOG = LoggerFactory.getLogger(VentaServiceImpl.class);

    private final String catedraVentaUrl;
    private final VentaMapper ventaMapper;
    private final String catedraBloqueoUrl;
    private final RestTemplate restTemplate;
    private final SesionService sesionService;
    private final UserRepository userRepository;
    private final VentaRepository ventaRepository;
    private final EventoRepository eventoRepository;
    private final AsientoVendidoRepository asientoVendidoRepository;

    public VentaServiceImpl(
        VentaMapper ventaMapper,
        RestTemplate restTemplate,
        SesionService sesionService,
        UserRepository userRepository,
        VentaRepository ventaRepository,
        EventoRepository eventoRepository,
        ApplicationProperties applicationProperties,
        AsientoVendidoRepository asientoVendidoRepository
    ) {
        this.ventaMapper = ventaMapper;
        this.restTemplate = restTemplate;
        this.sesionService = sesionService;
        this.userRepository = userRepository;
        this.ventaRepository = ventaRepository;
        this.eventoRepository = eventoRepository;
        this.asientoVendidoRepository = asientoVendidoRepository;
        this.catedraVentaUrl = applicationProperties.getCatedra().getVentaUrl();
        this.catedraBloqueoUrl = applicationProperties.getCatedra().getBloqueoUrl();
    }

    @Override
    public void bloquearAsientos(String login, SolicitudBloqueoDTO solicitudDTO) {
        LOG.debug("Solicitando bloqueo de asientos para usuario: {}", login);

        // 1. Construir Request para Cátedra
        BloqueoRequest request = new BloqueoRequest();
        request.setEventoId(solicitudDTO.getEventoId());

        List<AsientoBloqueoDTO> asientosCatedra = new ArrayList<>();
        for (AsientoSesionDTO a : solicitudDTO.getAsientos()) {
            asientosCatedra.add(new AsientoBloqueoDTO(a.getFila(), a.getColumna()));
        }
        request.setAsientos(asientosCatedra);

        // 2. Llamar a Cátedra
        BloqueoResponse response;
        try {
            response = restTemplate.postForObject(catedraBloqueoUrl, request, BloqueoResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al conectar con Cátedra para bloquear: " + e.getMessage());
        }

        if (response == null || !Boolean.TRUE.equals(response.getResultado())) {
            throw new RuntimeException("No se pudieron bloquear los asientos: " + (response != null ? response.getDescripcion() : "Error desconocido"));
        }

        // 3. Si el bloqueo fue exitoso, ACTUALIZAR LA SESIÓN EN REDIS
        actualizarSesionConBloqueo(login, solicitudDTO);
    }

    private void actualizarSesionConBloqueo(String login, SolicitudBloqueoDTO solicitud) {
        // Recuperamos o creamos la sesión
        SesionVentaDTO sesion = new SesionVentaDTO();
        sesion.setEventoId(solicitud.getEventoId());
        sesion.setAsientosSeleccionados(solicitud.getAsientos());
        sesion.setEstadoActual(EstadoSesion.CONFIRMANDO);

        // Guardamos en Redis usando el servicio de sesión que ya tenemos
        sesionService.guardarSesion(login, sesion);
        LOG.info("Sesión actualizada en Redis con asientos bloqueados para: {}", login);
    }

    @Override
    public VentaDTO realizarCompra(String login, ConfirmarCompraDTO compraDTO) {
        LOG.debug("Iniciando proceso de compra para usuario: {}", login);

        SesionVentaDTO sesion = recuperarSesion(login);

        Evento evento = recuperarEvento(sesion.getEventoId());

        VentaCatedraRequest requestCatedra = construirRequest(sesion,evento,compraDTO);

        VentaCatedraResponse respuestaCatedra = llamarApi(requestCatedra);

        Venta ventaLocal = guardarVenta(login,respuestaCatedra,evento);

        procesarResultadoVenta(login,ventaLocal,respuestaCatedra,compraDTO);

        return ventaMapper.toDto(ventaLocal);
    }

    // --- Helper Methods ---

    private SesionVentaDTO recuperarSesion(String login) {
        return sesionService.obtenerSesion(login)
            .orElseThrow(() -> new RuntimeException("No se encontró una sesión de compra activa."));
    }

    private Evento recuperarEvento(Long id) {
        return eventoRepository.findByEventoIdCatedra(id)
            .orElseThrow(() -> new RuntimeException("Evento no encontrado localmente. Sincronice los eventos primero."));
    }

    private VentaCatedraRequest construirRequest(SesionVentaDTO sesion, Evento evento, ConfirmarCompraDTO compraDTO){
        VentaCatedraRequest request = new VentaCatedraRequest();
        request.setEventoId(sesion.getEventoId());
        request.setFecha(java.time.Instant.now());
        request.setPrecioVenta(evento.getPrecioEntrada());

        List<AsientoVentaCatedraDTO> asientosCatedra = new ArrayList<>();
        for (ConfirmarCompraDTO.DetalleAsientoCompra detalle : compraDTO.getDetalles()) {
            asientosCatedra.add(new AsientoVentaCatedraDTO(
                detalle.getFila(),
                detalle.getColumna(),
                detalle.getNombrePersona()
            ));
        }
        request.setAsientos(asientosCatedra);
        return request;
    }

    private VentaCatedraResponse llamarApi(VentaCatedraRequest requestCatedra){
        try {
            LOG.info("Enviando solicitud de venta a Cátedra: {}", requestCatedra);
            VentaCatedraResponse response = restTemplate.postForObject(catedraVentaUrl, requestCatedra, VentaCatedraResponse.class);
            if (response == null) {
                throw new RuntimeException("Respuesta nula de la Cátedra");
            }
            return response;
        } catch (Exception e) {
            LOG.error("Error al llamar a Cátedra. Request enviado: precio={}, fecha={}", requestCatedra.getPrecioVenta(), requestCatedra.getFecha());
            throw new RuntimeException("Error al comunicarse con el servicio de ventas de la Cátedra: " + e.getMessage());
        }
    }

    private Venta guardarVenta(String login, VentaCatedraResponse response, Evento evento){
        User user = userRepository.findOneByLogin(login).orElseThrow();

        Venta ventaLocal = new Venta();
        ventaLocal.setUser(user);
        ventaLocal.setEvento(evento);
        ventaLocal.setFechaVenta(response.getFechaVenta());
        ventaLocal.setPrecioVenta(response.getPrecioVenta());
        ventaLocal.setResultado(response.getResultado());
        ventaLocal.setDescripcion(response.getDescripcion());

        if (Boolean.TRUE.equals(response.getResultado())) {
            ventaLocal.setVentaIdCatedra(response.getVentaId());
            ventaLocal.setEstadoVenta(EstadoVenta.CONFIRMADA);
        } else {
            ventaLocal.setEstadoVenta(EstadoVenta.RECHAZADA);
        }

        ventaLocal = ventaRepository.save(ventaLocal);
        return ventaLocal;
    }

    private void procesarResultadoVenta(String login, Venta ventaLocal ,VentaCatedraResponse response, ConfirmarCompraDTO compraDTO){
        if (Boolean.TRUE.equals(response.getResultado())) {
            for (ConfirmarCompraDTO.DetalleAsientoCompra detalle : compraDTO.getDetalles()) {
                AsientoVendido asiento = new AsientoVendido();
                asiento.setFila(detalle.getFila());
                asiento.setColumna(detalle.getColumna());
                asiento.setPersona(detalle.getNombrePersona());
                asiento.setVenta(ventaLocal);
                asientoVendidoRepository.save(asiento);
            }

            sesionService.borrarSesion(login);
            LOG.info("Compra realizada con éxito. Venta ID local: {}", ventaLocal.getId());
        } else {
            LOG.warn("La venta fue rechazada por la Cátedra: {}", response.getDescripcion());
        }
    }

    // -------------

    @Override
    public VentaDTO save(VentaDTO ventaDTO) {
        LOG.debug("Request to save Venta : {}", ventaDTO);
        Venta venta = ventaMapper.toEntity(ventaDTO);
        venta = ventaRepository.save(venta);
        return ventaMapper.toDto(venta);
    }

    @Override
    public VentaDTO update(VentaDTO ventaDTO) {
        LOG.debug("Request to update Venta : {}", ventaDTO);
        Venta venta = ventaMapper.toEntity(ventaDTO);
        venta = ventaRepository.save(venta);
        return ventaMapper.toDto(venta);
    }

    @Override
    public Optional<VentaDTO> partialUpdate(VentaDTO ventaDTO) {
        LOG.debug("Request to partially update Venta : {}", ventaDTO);

        return ventaRepository
            .findById(ventaDTO.getId())
            .map(existingVenta -> {
                ventaMapper.partialUpdate(existingVenta, ventaDTO);

                return existingVenta;
            })
            .map(ventaRepository::save)
            .map(ventaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaDTO> findAll() {
        LOG.debug("Request to get all Ventas");
        return ventaRepository.findAll().stream().map(ventaMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<VentaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return ventaRepository.findAllWithEagerRelationships(pageable).map(ventaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VentaDTO> findOne(Long id) {
        LOG.debug("Request to get Venta : {}", id);
        return ventaRepository.findOneWithEagerRelationships(id).map(ventaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Venta : {}", id);
        ventaRepository.deleteById(id);
    }
}
