package com.um.programacion2.proxy.service;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class AsientoService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * Obtiene el mapa de asientos (Hash) desde el Redis de la Cátedra.
    **/
    public Map<Object, Object> obtenerAsientosPorEvento(Long eventoId) {
        // La clave que consultaremos en el Redis de la Cátedra
        String claveRedis = "evento:" + eventoId;

        // .entries() obtiene todos los campos y valores del Hash en esa clave
        return redisTemplate.opsForHash().entries(claveRedis);
    }
}
