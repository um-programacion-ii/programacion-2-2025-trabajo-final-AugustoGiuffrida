package com.um.programacion2.trabajo_final.web.rest;

import com.um.programacion2.trabajo_final.domain.Evento;
import com.um.programacion2.trabajo_final.repository.EventoRepository;
import com.um.programacion2.trabajo_final.service.ProxyService;
import com.um.programacion2.trabajo_final.service.dto.proxy.EventoRedisDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/api/asientos-ocupados")
public class AsientosOcupadoResource {

    private final Logger log = LoggerFactory.getLogger(AsientosOcupadoResource.class);
    private final EventoRepository eventoRepository;
    private final ProxyService proxyService;

    public AsientosOcupadoResource(EventoRepository eventoRepository, ProxyService proxyService) {
        this.eventoRepository = eventoRepository;
        this.proxyService = proxyService;
    }

    /**
     * GET /api/asientos-ocupados/eventos/{id}: Obtener asientos ocupados de un evento.
     */
    @GetMapping("/eventos/{id}")
    public ResponseEntity<EventoRedisDTO> getAsientosOcupados(@PathVariable Long id) {
        log.debug("REST request para obtener asientos ocupados del evento : {}", id);
        Evento evento = eventoRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento no encontrado"));

        Long idRealCatedra = evento.getEventoIdCatedra();

        Optional<EventoRedisDTO> result = proxyService.obtenerAsientos(idRealCatedra);

        return result.map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.noContent().build());
    }
}
