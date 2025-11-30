package com.um.programacion2.proxy.web.rest;

import com.um.programacion2.proxy.service.AsientoService;
import java.util.Map;

import com.um.programacion2.proxy.service.dto.EventoRedisDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AsientoController {

    private final Logger log = LoggerFactory.getLogger(AsientoController.class);


    @Autowired
    private AsientoService asientoService;

    /**
     * Endpoint para que el Backend consulte el estado de los asientos de un evento.
     *
     * @param eventoId El ID del evento a consultar.
     * @return un Map con los asientos y sus estados.
     */
    @GetMapping("/asientos/{eventoId}")
    public ResponseEntity<EventoRedisDTO> getAsientos(@PathVariable Long eventoId) {
        log.debug("REST request para obtener asientos para el evento: {}", eventoId);

        EventoRedisDTO asientos = asientoService.obtenerAsientosPorEvento(eventoId);

        if (asientos == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(asientos);
    }
}