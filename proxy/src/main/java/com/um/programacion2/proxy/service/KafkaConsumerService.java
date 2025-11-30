package com.um.programacion2.proxy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private final Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);

    /**
     * Escucha los mensajes de Kafka del tópico 'eventos-actualizacion'.
     */
    @KafkaListener(topics = "eventos-actualizacion", groupId = "${spring.kafka.consumer.group-id}")
    public void consumirMensaje(String mensaje) {
        log.info("[KAFKA] Actualización recibida: {}", mensaje);

        // TODO: Avisar Backend que debe actualizarse.
    }
}