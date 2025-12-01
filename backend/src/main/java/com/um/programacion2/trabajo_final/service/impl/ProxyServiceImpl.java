package com.um.programacion2.trabajo_final.service.impl;

import com.um.programacion2.trabajo_final.config.ApplicationProperties;
import com.um.programacion2.trabajo_final.service.ProxyService;
import com.um.programacion2.trabajo_final.service.dto.proxy.EventoRedisDTO;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@Transactional
public class ProxyServiceImpl  implements ProxyService {
    private final Logger log = LoggerFactory.getLogger(ProxyServiceImpl.class);
    private final RestTemplate restTemplate;
    private final String proxyUrl;

    public ProxyServiceImpl(RestTemplate restTemplate, ApplicationProperties applicationProperties) {
        this.restTemplate = restTemplate;
        this.proxyUrl = applicationProperties.getCatedra().getProxyUrl();
    }

    @Override
    public Optional<EventoRedisDTO> obtenerAsientos(Long eventoId) {
        log.debug("Solicitando asientos al Proxy para evento: {}", eventoId);
        try {
            EventoRedisDTO response = restTemplate.getForObject(
                proxyUrl + "/asientos/" + eventoId,
                EventoRedisDTO.class
            );
            return Optional.ofNullable(response);
        } catch (HttpClientErrorException.NotFound e) {
            log.debug("El Proxy no encontró asientos ocupados para el evento {}", eventoId);
            return Optional.empty();
        } catch (Exception e) {
            log.error("Error al comunicar con el Proxy: {}", e.getMessage());
            return Optional.empty();
        }
    }
}
