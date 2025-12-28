package com.um.programacion2.trabajo_final.service;

import com.um.programacion2.trabajo_final.service.dto.proxy.EventoRedisDTO;
import java.util.Optional;

public interface ProxyService {
    Optional<EventoRedisDTO> obtenerAsientos(Long eventoId);
}
