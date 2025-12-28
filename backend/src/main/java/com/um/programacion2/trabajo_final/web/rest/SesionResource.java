package com.um.programacion2.trabajo_final.web.rest;

import com.um.programacion2.trabajo_final.security.SecurityUtils;
import com.um.programacion2.trabajo_final.service.SesionService;
import com.um.programacion2.trabajo_final.service.dto.SesionVentaDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/sesion")
public class SesionResource {

    private final Logger log = LoggerFactory.getLogger(SesionResource.class);
    private final SesionService sesionService;

    public SesionResource(SesionService sesionService) {
        this.sesionService = sesionService;
    }

    /**
     * POST /api/sesion : Guarda o actualiza el estado actual de la compra.
     */
    @PostMapping
    public ResponseEntity<Void> guardarSesion(@RequestBody SesionVentaDTO sesionDTO) {
        // Obtenemos el login del usuario autenticado actual
        String login = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new RuntimeException("Usuario no autenticado"));

        log.debug("Request para guardar sesión de compra para: {}", login);
        sesionService.guardarSesion(login, sesionDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * GET /api/sesion : Obtiene el estado guardado (si existe).
     */
    @GetMapping
    public ResponseEntity<SesionVentaDTO> obtenerSesion() {
        String login = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new RuntimeException("Usuario no autenticado"));

        log.debug("Request para obtener sesión de compra para: {}", login);
        Optional<SesionVentaDTO> sesion = sesionService.obtenerSesion(login);

        return sesion.map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.noContent().build());
    }

    /**
     * DELETE /api/sesion : Elimina la sesión (ej: al finalizar compra o cancelar).
     */
    @DeleteMapping
    public ResponseEntity<Void> borrarSesion() {
        String login = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new RuntimeException("Usuario no autenticado"));

        log.debug("Request para borrar sesión de compra para: {}", login);
        sesionService.borrarSesion(login);
        return ResponseEntity.ok().build();
    }
}
