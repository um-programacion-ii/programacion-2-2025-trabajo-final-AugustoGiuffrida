package com.um.programacion2.proxy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment; // <--- IMPORTAR ESTO
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KafkaConsumerService {

    private final Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);
    private final RestTemplate restTemplate;
    private final String backendUrl;

    public KafkaConsumerService(

        @Value("${application.backend-url}")
        String backendUrl,
        RestTemplate restTemplate
        ) {
        this.restTemplate = restTemplate;
        this.backendUrl = backendUrl;
    }

    /**
     * Escucha los mensajes de Kafka y confirma MANUALMENTE.
     */
    @KafkaListener(topics = "eventos-actualizacion", groupId = "${spring.kafka.consumer.group-id}")
    public void consumirMensaje(String mensaje, Acknowledgment ack) {
        log.info("[KAFKA] Recibido mensaje: {}", mensaje);

        try {
            // 1. Validar que tengamos el objeto ack
            if (ack == null) {
                log.error("X [KAFKA] Error Crítico: El objeto 'ack' es NULO. Revisa la configuración ack-mode.");
                return;
            }

            // 2. Procesar (Llamar al Backend)
            String url = backendUrl + "/api/eventos/notificacion-cambio";
            restTemplate.postForLocation(url, mensaje);
            log.info("[PROXY] Notificación enviada al Backend.");

            // 3. Confirmar (Commit)
            try {
                ack.acknowledge();
                log.info("[KAFKA] Commit enviado (acknowledge ejecutado).");
            } catch (Exception eCommit) {
                log.error("[KAFKA] Falló el commit a Kafka: {}", eCommit.getMessage());
            }

        } catch (Exception e) {
            log.error("X [PROXY] Error procesando mensaje: {}", e.getMessage());
            if (ack != null) ack.acknowledge();
        }
    }
}