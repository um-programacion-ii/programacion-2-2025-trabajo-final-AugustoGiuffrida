package com.um.programacion2.proxy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KafkaConsumerService {

    private final String backendUrl;
    private final Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);
    private final RestTemplate restTemplate;

    public KafkaConsumerService(
        RestTemplate restTemplate,
        @Value("${application.backend-url}") String backendUrl
    ) {
        this.backendUrl = backendUrl;
        this.restTemplate = restTemplate;
    }

    /**
     * Escucha los mensajes de Kafka del tópico 'eventos-actualizacion'.
     */
    @KafkaListener(topics = "eventos-actualizacion", groupId = "${spring.kafka.consumer.group-id}")
    public void consumirMensaje(String mensaje) {
        log.info("[KAFKA] Actualización recibida: {}", mensaje);

        try {
            String url = backendUrl + "/api/eventos/notificacion-cambio";
            log.debug("Notificando al backend en: {}", url);

            restTemplate.postForLocation(url, mensaje);

            log.info("[PROXY] Notificación enviada al Backend correctamente.");
        } catch (Exception e) {
            log.error("[PROXY] Error al notificar al Backend: {}", e.getMessage());
        }

    }
}