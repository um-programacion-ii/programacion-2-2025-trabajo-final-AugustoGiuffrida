package com.um.programacion2.trabajo_final.service.dto;

import com.um.programacion2.trabajo_final.enumeration.EstadoSesion;

import java.io.Serializable;
import java.util.List;

/**
 * DTO para almacenar el estado temporal de una compra en Redis.
 */
public class SesionVentaDTO implements Serializable {

    private Long eventoId;
    private String nombreEvento;
    private List<AsientoSesionDTO> asientosSeleccionados;
    private EstadoSesion estadoActual; //  "SELECCIONANDO", "CONFIRMANDO"

    public Long getEventoId() {
        return eventoId;
    }
    public void setEventoId(Long eventoId) {
        this.eventoId = eventoId;
    }

    public String getNombreEvento() { return nombreEvento; }
    public void setNombreEvento(String nombreEvento) {
        this.nombreEvento = nombreEvento;
    }

    public List<AsientoSesionDTO> getAsientosSeleccionados() {
        return asientosSeleccionados;
    }
    public void setAsientosSeleccionados(List<AsientoSesionDTO> asientosSeleccionados) {
        this.asientosSeleccionados = asientosSeleccionados;
    }

    public EstadoSesion getEstadoActual() {
        return estadoActual;
    }
    public void setEstadoActual(EstadoSesion estadoActual) {
        this.estadoActual = estadoActual;
    }

}
