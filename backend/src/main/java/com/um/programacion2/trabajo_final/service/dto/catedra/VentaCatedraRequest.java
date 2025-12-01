package com.um.programacion2.trabajo_final.service.dto.catedra;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class VentaCatedraRequest {
    private Long eventoId;
    private List<AsientoVentaCatedraDTO> asientos;
    private Instant fecha;
    private BigDecimal precioVenta;

    public Long getEventoId() {
        return eventoId;
    }

    public void setEventoId(Long eventoId) {
        this.eventoId = eventoId;
    }

    public Instant getFecha() {
        return fecha;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }

    public List<AsientoVentaCatedraDTO> getAsientos() {
        return asientos;
    }

    public void setAsientos(List<AsientoVentaCatedraDTO> asientos) {
        this.asientos = asientos;
    }
}
