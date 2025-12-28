package com.um.programacion2.trabajo_final.service.impl;

import com.um.programacion2.trabajo_final.config.ApplicationProperties;
import com.um.programacion2.trabajo_final.service.ProxyService;
import com.um.programacion2.trabajo_final.service.dto.proxy.AsientoRedisDTO;
import com.um.programacion2.trabajo_final.service.dto.proxy.EventoRedisDTO;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Optional<EventoRedisDTO> obtenerAsientos(Long eventoId) { //asientos ocupados
        log.debug("Solicitando asientos al Proxy para evento: {}", eventoId);
        try {
            EventoRedisDTO response = restTemplate.getForObject(
                proxyUrl + "/asientos/" + eventoId,
                EventoRedisDTO.class
            );

            if (response != null && response.getAsientos() != null) {
                filtrarAsientosExpirados(response);
            }

            return Optional.ofNullable(response);
        } catch (HttpClientErrorException.NotFound e) {
            log.debug("El Proxy no encontró asientos ocupados para el evento {}", eventoId);
            return Optional.empty();
        } catch (Exception e) {
            log.error("Error al comunicar con el Proxy: {}", e.getMessage());
            return Optional.empty();
        }
    }

    private void filtrarAsientosExpirados(EventoRedisDTO eventoRedis) {
        Instant ahora = Instant.now();

        List<AsientoRedisDTO> ocupadosReales = eventoRedis.getAsientos().stream()
            .filter(asiento -> esAsientoRealmenteOcupado(asiento, ahora))
            .collect(Collectors.toList());

        eventoRedis.setAsientos(ocupadosReales);
    }

    private boolean esAsientoRealmenteOcupado(AsientoRedisDTO asiento, Instant ahora) {
        if ("Vendido".equalsIgnoreCase(asiento.getEstado())) {
            return true;
        }

        // Si está bloqueado, verificamos la fecha de expiración
        if ("Bloqueado".equalsIgnoreCase(asiento.getEstado())) {
            if (asiento.getExpira() == null) {
                return true; // Ante la duda, asumir ocupado?
            }
            try {
                Instant expiracion = Instant.parse(asiento.getExpira());
                // expiracion -> POSTERIOR -> ahora,-> bloqueado (true).
                // expiracion -> ANTERIOR -> ahora, -> libre (false).  expiró
                return expiracion.isAfter(ahora);
            } catch (Exception e) {
                log.warn("Error parseando fecha expiración asiento: {}", e.getMessage());
                return true;
            }
        }
        return false;
    }
}
