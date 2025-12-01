package com.um.programacion2.trabajo_final.service.impl;

import com.um.programacion2.trabajo_final.domain.AsientoVendido;
import com.um.programacion2.trabajo_final.domain.Evento;
import com.um.programacion2.trabajo_final.domain.User;
import com.um.programacion2.trabajo_final.domain.Venta;
import com.um.programacion2.trabajo_final.enumeration.EstadoVenta;
import com.um.programacion2.trabajo_final.repository.AsientoVendidoRepository;
import com.um.programacion2.trabajo_final.repository.EventoRepository;
import com.um.programacion2.trabajo_final.repository.UserRepository;
import com.um.programacion2.trabajo_final.repository.VentaRepository;
import com.um.programacion2.trabajo_final.service.SesionService;
import com.um.programacion2.trabajo_final.service.VentaService;
import com.um.programacion2.trabajo_final.service.dto.ConfirmarCompraDTO;
import com.um.programacion2.trabajo_final.service.dto.SesionVentaDTO;
import com.um.programacion2.trabajo_final.service.dto.VentaDTO;
import com.um.programacion2.trabajo_final.service.dto.catedra.AsientoVentaCatedraDTO;
import com.um.programacion2.trabajo_final.service.dto.catedra.VentaCatedraRequest;
import com.um.programacion2.trabajo_final.service.dto.catedra.VentaCatedraResponse;
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

/**
 * Service Implementation for managing {@link com.um.programacion2.trabajo_final.domain.Venta}.
 */
@Service
@Transactional
public class VentaServiceImpl implements VentaService {

    private static final Logger LOG = LoggerFactory.getLogger(VentaServiceImpl.class);

    private final VentaRepository ventaRepository;
    private final VentaMapper ventaMapper;
    private final AsientoVendidoRepository asientoVendidoRepository;
    private final EventoRepository eventoRepository;
    private final UserRepository userRepository;
    private final SesionService sesionService;
    private final RestTemplate restTemplate;
    private final String catedraVentaUrl;

    public VentaServiceImpl(VentaRepository ventaRepository, VentaMapper ventaMapper, AsientoVendidoRepository asientoVendidoRepository, EventoRepository eventoRepository, UserRepository userRepository, SesionService sesionService, RestTemplate restTemplate, com.um.programacion2.trabajo_final.config.ApplicationProperties applicationProperties) {
        this.ventaRepository = ventaRepository;
        this.ventaMapper = ventaMapper;
        this.asientoVendidoRepository = asientoVendidoRepository;
        this.eventoRepository = eventoRepository;
        this.userRepository = userRepository;
        this.sesionService = sesionService;
        this.restTemplate = restTemplate;
        this.catedraVentaUrl = applicationProperties.getCatedra().getVentaUrl();;
    }

    @Override
    public VentaDTO realizarCompra(String login, ConfirmarCompraDTO compraDTO) {
        LOG.debug("Iniciando proceso de compra para usuario: {}", login);

        // 1. Recuperar la sesión del usuario
        SesionVentaDTO sesion = sesionService.obtenerSesion(login)
            .orElseThrow(() -> new RuntimeException("No se encontró una sesión de compra activa."));

        // 2. [CAMBIO] Recuperar el Evento localmente AHORA para obtener el precio
        Evento evento = eventoRepository.findByEventoIdCatedra(sesion.getEventoId())
            .orElseThrow(() -> new RuntimeException("Evento no encontrado localmente. Sincronice los eventos primero."));

        // 3. Construir el Request para la Cátedra (Ahora con fecha y precio)
        VentaCatedraRequest requestCatedra = new VentaCatedraRequest();
        requestCatedra.setEventoId(sesion.getEventoId());
        requestCatedra.setFecha(java.time.Instant.now()); // <-- Agregamos Fecha actual
        requestCatedra.setPrecioVenta(evento.getPrecioEntrada()); // <-- Agregamos el Precio del evento

        List<AsientoVentaCatedraDTO> asientosCatedra = new ArrayList<>();
        for (ConfirmarCompraDTO.DetalleAsientoCompra detalle : compraDTO.getDetalles()) {
            asientosCatedra.add(new AsientoVentaCatedraDTO(
                detalle.getFila(),
                detalle.getColumna(),
                detalle.getNombrePersona()
            ));
        }
        requestCatedra.setAsientos(asientosCatedra);

        // 4. Llamar a la API de la Cátedra
        VentaCatedraResponse respuestaCatedra;
        try {
            LOG.info("Enviando solicitud de venta a Cátedra: {}", requestCatedra);
            respuestaCatedra = restTemplate.postForObject(catedraVentaUrl, requestCatedra, VentaCatedraResponse.class);
        } catch (Exception e) {
            // Tip: Esto te ayudará a ver si la cátedra devuelve un error legible
            LOG.error("Error al llamar a Cátedra. Request enviado: precio={}, fecha={}", requestCatedra.getPrecioVenta(), requestCatedra.getFecha());
            throw new RuntimeException("Error al comunicarse con el servicio de ventas de la Cátedra: " + e.getMessage());
        }

        if (respuestaCatedra == null) {
            throw new RuntimeException("Respuesta nula de la Cátedra");
        }

        // 5. Guardar la Venta en Local (MySQL)
        User user = userRepository.findOneByLogin(login).orElseThrow();
        // (Ya tenemos la variable 'evento' recuperada en el paso 2)

        Venta ventaLocal = new Venta();
        ventaLocal.setUser(user);
        ventaLocal.setEvento(evento);
        ventaLocal.setFechaVenta(respuestaCatedra.getFechaVenta());
        ventaLocal.setPrecioVenta(respuestaCatedra.getPrecioVenta());
        ventaLocal.setResultado(respuestaCatedra.getResultado());
        ventaLocal.setDescripcion(respuestaCatedra.getDescripcion());

        // Si fue exitosa, guardamos el ID de cátedra y estado Confirmada
        if (Boolean.TRUE.equals(respuestaCatedra.getResultado())) {
            ventaLocal.setVentaIdCatedra(respuestaCatedra.getVentaId());
            ventaLocal.setEstadoVenta(EstadoVenta.CONFIRMADA);
        } else {
            ventaLocal.setEstadoVenta(EstadoVenta.RECHAZADA);
        }

        ventaLocal = ventaRepository.save(ventaLocal);

        // 6. Guardar los Asientos Vendidos (Detalle)
        if (Boolean.TRUE.equals(respuestaCatedra.getResultado())) {
            for (ConfirmarCompraDTO.DetalleAsientoCompra detalle : compraDTO.getDetalles()) {
                AsientoVendido asiento = new AsientoVendido();
                asiento.setFila(detalle.getFila());
                asiento.setColumna(detalle.getColumna());
                asiento.setPersona(detalle.getNombrePersona());
                asiento.setVenta(ventaLocal);
                asientoVendidoRepository.save(asiento);
            }

            // 7. Limpiar la sesión solo si fue exitoso
            sesionService.borrarSesion(login);
            LOG.info("Compra realizada con éxito. Venta ID local: {}", ventaLocal.getId());
        } else {
            LOG.warn("La venta fue rechazada por la Cátedra: {}", respuestaCatedra.getDescripcion());
        }

        return ventaMapper.toDto(ventaLocal);
    }

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
