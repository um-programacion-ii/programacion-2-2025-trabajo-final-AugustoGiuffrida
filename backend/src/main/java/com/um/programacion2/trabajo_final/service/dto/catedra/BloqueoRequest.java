package com.um.programacion2.trabajo_final.service.dto.catedra;

import java.util.List;

public class BloqueoRequest {
    private Long eventoId;
    private List<AsientoBloqueoDTO> asientos;

    public Long getEventoId() {
        return eventoId;
    }

    public void setEventoId(Long eventoId) {
        this.eventoId = eventoId;
    }

    public List<AsientoBloqueoDTO> getAsientos() {
        return asientos;
    }

    public void setAsientos(List<AsientoBloqueoDTO> asientos) {
        this.asientos = asientos;
    }
}
