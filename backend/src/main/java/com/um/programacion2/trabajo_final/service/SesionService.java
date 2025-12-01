package com.um.programacion2.trabajo_final.service;

import com.um.programacion2.trabajo_final.service.dto.SesionVentaDTO;
import java.util.Optional;

public interface SesionService {
    void guardarSesion(String login, SesionVentaDTO sesion);
    Optional<SesionVentaDTO> obtenerSesion(String login);
    void borrarSesion(String login);
}
