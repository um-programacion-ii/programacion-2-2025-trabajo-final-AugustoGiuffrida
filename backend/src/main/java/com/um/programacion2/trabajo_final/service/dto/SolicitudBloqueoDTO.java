package com.um.programacion2.trabajo_final.service.dto;

import java.util.List;

public class SolicitudBloqueoDTO {
    private Long eventoId;
    private List<AsientoSesionDTO> asientos;

    public Long getEventoId() {
        return eventoId;
    }

    public void setEventoId(Long eventoId) {
        this.eventoId = eventoId;
    }

    public List<AsientoSesionDTO> getAsientos() {
        return asientos;
    }

    public void setAsientos(List<AsientoSesionDTO> asientos) {
        this.asientos = asientos;
    }
}
