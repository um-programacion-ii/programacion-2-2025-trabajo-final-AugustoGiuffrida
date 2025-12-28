package com.um.programacion2.trabajo_final.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.um.programacion2.trabajo_final.domain.AsientoVendido} entity.
 */
@Schema(description = "Entidad auxiliar para guardar cada asiento de una Venta.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AsientoVendidoDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer fila;

    @NotNull
    private Integer columna;

    @NotNull
    private String persona;

    private VentaDTO venta;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getFila() {
        return fila;
    }

    public void setFila(Integer fila) {
        this.fila = fila;
    }

    public Integer getColumna() {
        return columna;
    }

    public void setColumna(Integer columna) {
        this.columna = columna;
    }

    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }

    public VentaDTO getVenta() {
        return venta;
    }

    public void setVenta(VentaDTO venta) {
        this.venta = venta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AsientoVendidoDTO)) {
            return false;
        }

        AsientoVendidoDTO asientoVendidoDTO = (AsientoVendidoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, asientoVendidoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AsientoVendidoDTO{" +
            "id=" + getId() +
            ", fila=" + getFila() +
            ", columna=" + getColumna() +
            ", persona='" + getPersona() + "'" +
            ", venta=" + getVenta() +
            "}";
    }
}
