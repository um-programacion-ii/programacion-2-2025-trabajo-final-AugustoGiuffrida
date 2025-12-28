package com.um.programacion2.trabajo_final.service.dto.proxy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventoRedisDTO {
    private Long eventoId;
    private List<AsientoRedisDTO> asientos;

    public Long getEventoId() {
        return eventoId;
    }
    public void setEventoId(Long eventoId) {
        this.eventoId = eventoId;
    }
    public List<AsientoRedisDTO> getAsientos() {
        return asientos;
    }
    public void setAsientos(List<AsientoRedisDTO> asientos) {
        this.asientos = asientos;
    }
}
