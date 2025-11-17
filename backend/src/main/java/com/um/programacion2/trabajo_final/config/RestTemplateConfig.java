package com.um.programacion2.trabajo_final.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    // 1. Lee el token que guardamos en el application-dev.yml
    @Value("${application.catedra.token}")
    private String catedraToken;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // 2. Agrega un "interceptor" que se ejecuta ANTES de cada llamada.
        restTemplate.getInterceptors().add((request, body, execution) -> {

            // 3. Añade el Header de Autorización con el token.
            // Esto cumple con el requisito de autenticar todas las llamadas
            request.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + catedraToken);
            return execution.execute(request, body);
        });

        return restTemplate;
    }
}
