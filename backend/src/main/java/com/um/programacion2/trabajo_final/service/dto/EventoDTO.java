package com.um.programacion2.trabajo_final.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.um.programacion2.trabajo_final.domain.Evento} entity.
 */
@Schema(description = "Entidad para almacenar localmente la información de los eventos\nsincronizados desde el servicio de la Cátedra.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventoDTO implements Serializable {

    private Long id;

    @NotNull
    private Long eventoIdCatedra;

    @NotNull
    private String titulo;

    private String resumen;

    private String descripcion;

    @NotNull
    private Instant fecha;

    private String direccion;

    private String imagen;

    @NotNull
    private Integer filaAsientos;

    @NotNull
    private Integer columnAsientos;

    @NotNull
    private BigDecimal precioEntrada;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEventoIdCatedra() {
        return eventoIdCatedra;
    }

    public void setEventoIdCatedra(Long eventoIdCatedra) {
        this.eventoIdCatedra = eventoIdCatedra;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Instant getFecha() {
        return fecha;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Integer getFilaAsientos() {
        return filaAsientos;
    }

    public void setFilaAsientos(Integer filaAsientos) {
        this.filaAsientos = filaAsientos;
    }

    public Integer getColumnAsientos() {
        return columnAsientos;
    }

    public void setColumnAsientos(Integer columnAsientos) {
        this.columnAsientos = columnAsientos;
    }

    public BigDecimal getPrecioEntrada() {
        return precioEntrada;
    }

    public void setPrecioEntrada(BigDecimal precioEntrada) {
        this.precioEntrada = precioEntrada;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventoDTO)) {
            return false;
        }

        EventoDTO eventoDTO = (EventoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, eventoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventoDTO{" +
            "id=" + getId() +
            ", eventoIdCatedra=" + getEventoIdCatedra() +
            ", titulo='" + getTitulo() + "'" +
            ", resumen='" + getResumen() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", fecha='" + getFecha() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", imagen='" + getImagen() + "'" +
            ", filaAsientos=" + getFilaAsientos() +
            ", columnAsientos=" + getColumnAsientos() +
            ", precioEntrada=" + getPrecioEntrada() +
            "}";
    }
}
