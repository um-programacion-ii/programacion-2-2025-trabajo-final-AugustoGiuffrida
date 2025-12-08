package com.um.programacion2.proxy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CatedraGatewayService {

    private final Logger log = LoggerFactory.getLogger(CatedraGatewayService.class);
    private final RestTemplate restTemplate;
    private final String catedraUrl;

    public CatedraGatewayService(
        RestTemplate restTemplate,
        @Value("${application.catedra-api-url}") String catedraUrl
    ) {
        this.restTemplate = restTemplate;
        this.catedraUrl = catedraUrl;
    }

    /**
     * Reenvía una petición POST a la Cátedra.
     * @param path El endpoint relativo
     * @param body El cuerpo del mensaje (JSON)
     * @param headers Las cabeceras originales (para mantener el Token de autorización si viene del backend)
     */
        public ResponseEntity<Object> forwardPost(String path, Object body, HttpHeaders headers) {
            String urlDestino = catedraUrl + path;
            log.info("Reenviando petición a Cátedra: {}", urlDestino);

            try {
                HttpHeaders newHeaders = new HttpHeaders();

                // 2. Solo copia el Content-Type y la Autorización
                if (headers.getContentType() != null) {
                    newHeaders.setContentType(headers.getContentType());
                } else {
                    newHeaders.setContentType(MediaType.APPLICATION_JSON);
                }

                // Copiar el Token que viene del Backend
                if (headers.containsKey(HttpHeaders.AUTHORIZATION)) {
                    newHeaders.set(HttpHeaders.AUTHORIZATION, headers.getFirst(HttpHeaders.AUTHORIZATION));
                }

                HttpEntity<Object> requestEntity = new HttpEntity<>(body, newHeaders);

                return restTemplate.exchange(
                        urlDestino,
                        HttpMethod.POST,
                        requestEntity,
                        Object.class
                );

            } catch (Exception e) {
                log.error("Error al comunicarse con Cátedra: {}", e.getMessage());
                throw new RuntimeException("Error en la pasarela hacia Cátedra: " + e.getMessage());
            }
        }
}
