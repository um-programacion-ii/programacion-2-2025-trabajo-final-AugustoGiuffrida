package com.um.programacion2.trabajo_final.web.rest;

import com.um.programacion2.trabajo_final.service.ProxyService;
import com.um.programacion2.trabajo_final.service.dto.proxy.EventoRedisDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/asientos-ocupados")
public class AsientosDisponibleResource {

    private final Logger log = LoggerFactory.getLogger(AsientosDisponibleResource.class);

    private final ProxyService proxyService;

    public AsientosDisponibleResource(ProxyService proxyService) {
        this.proxyService = proxyService;
    }

    /**
     * GET /api/asientos-ocupados/eventos/{id}: Obtener asientos ocupados de un evento.
     */
    @GetMapping("/eventos/{id}")
    public ResponseEntity<EventoRedisDTO> getAsientosOcupados(@PathVariable Long id) {
        log.debug("REST request para obtener asientos ocupados del evento : {}", id);

        Optional<EventoRedisDTO> result = proxyService.obtenerAsientos(id);

        return result.map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.noContent().build());
    }
}
