package com.um.programacion2.trabajo_final.service.dto.catedra;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true) // Ignora campos del JSON que no nos interesan
public class EventoCatedraDTO {

    // Mapeamos el 'id' del JSON de cátedra
    private Long id;
    private String titulo;
    private String resumen;
    private String descripcion;
    private Instant fecha;
    private String direccion;
    private String imagen;
    private Integer filaAsientos;
    private Integer columnAsientos;
    private BigDecimal precioEntrada;
    private EventoTipoDTO eventoTipo;
    private List<IntegranteDTO> integrantes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public EventoTipoDTO getEventoTipo() {
        return eventoTipo;
    }

    public void setEventoTipo(EventoTipoDTO eventoTipo) {
        this.eventoTipo = eventoTipo;
    }

    public List<IntegranteDTO> getIntegrantes() {
        return integrantes;
    }

    public void setIntegrantes(List<IntegranteDTO> integrantes) {
        this.integrantes = integrantes;
    }
}
