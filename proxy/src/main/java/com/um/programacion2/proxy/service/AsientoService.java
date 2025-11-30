package com.um.programacion2.proxy.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.um.programacion2.proxy.service.dto.EventoRedisDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class AsientoService {

    private final Logger log = LoggerFactory.getLogger(AsientoService.class);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper; // Jackson para convertir JSON a Objetos

    /**
     * Obtiene el objeto de asientos desde el Redis de la Cátedra.
     * Clave: "evento_<id>"
     * Valor: JSON String
     */
    public EventoRedisDTO obtenerAsientosPorEvento(Long eventoId) {
        // 1. Construimos la clave según la especificación: "evento_ID"
        String claveRedis = "evento_" + eventoId;

        // 2. Obtenemos el valor como String (Metodo operador para strings -> opsForValue)
        String rawJson = redisTemplate.opsForValue().get(claveRedis);

        if (rawJson == null) {
            log.warn("No se encontraron datos en Redis para la clave: {}", claveRedis);
            return null;
        }

        try {
            // 3. Convertimos el String JSON a nuestro objeto Java
            return objectMapper.readValue(rawJson, EventoRedisDTO.class);
        } catch (JsonProcessingException e) {
            log.error("Error al parsear el JSON de Redis: {}", e.getMessage());
            throw new RuntimeException("Error al leer datos de Redis", e);
        }
    }
}

//Por ejemplo, en la base de datos se ve así:
// Clave: evento_1
//Valor: "{\"eventoId\":1,\"asientos\":[...]}"
