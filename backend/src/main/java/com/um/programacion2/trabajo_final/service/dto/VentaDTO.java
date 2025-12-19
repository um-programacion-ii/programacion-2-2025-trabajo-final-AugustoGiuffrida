package com.um.programacion2.trabajo_final.service.dto;

import com.um.programacion2.trabajo_final.enumeration.EstadoVenta;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.um.programacion2.trabajo_final.domain.Venta} entity.
 */
@Schema(description = "Entidad para registrar las ventas realizadas.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VentaDTO implements Serializable {

    private Long id;

    private Long ventaIdCatedra;

    @NotNull
    private Instant fechaVenta;

    @NotNull
    private BigDecimal precioVenta;

    @NotNull
    private Boolean resultado;

    private String descripcion;

    private EstadoVenta estadoVenta;

    private Set<AsientoVendidoDTO> asientos = new HashSet<>();

    @NotNull
    @Schema(description = "Una Venta pertenece a un solo Evento.")
    private EventoDTO evento;

    @NotNull
    @Schema(description = "Una Venta es realizada por un solo Usuario (User).\nEl User ya existe en JHipster.")
    private UserDTO user;

    public Set<AsientoVendidoDTO> getAsientos() {
        return asientos;
    }

    public void setAsientos(Set<AsientoVendidoDTO> asientos) {
        this.asientos = asientos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVentaIdCatedra() {
        return ventaIdCatedra;
    }

    public void setVentaIdCatedra(Long ventaIdCatedra) {
        this.ventaIdCatedra = ventaIdCatedra;
    }

    public Instant getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(Instant fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Boolean getResultado() {
        return resultado;
    }

    public void setResultado(Boolean resultado) {
        this.resultado = resultado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public EstadoVenta getEstadoVenta() {
        return estadoVenta;
    }

    public void setEstadoVenta(EstadoVenta estadoVenta) {
        this.estadoVenta = estadoVenta;
    }

    public EventoDTO getEvento() {
        return evento;
    }

    public void setEvento(EventoDTO evento) {
        this.evento = evento;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VentaDTO)) {
            return false;
        }

        VentaDTO ventaDTO = (VentaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ventaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VentaDTO{" +
            "id=" + getId() +
            ", ventaIdCatedra=" + getVentaIdCatedra() +
            ", fechaVenta='" + getFechaVenta() + "'" +
            ", precioVenta=" + getPrecioVenta() +
            ", resultado='" + getResultado() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", estadoVenta='" + getEstadoVenta() + "'" +
            ", evento=" + getEvento() +
            ", user=" + getUser() +
            "}";
    }
}
