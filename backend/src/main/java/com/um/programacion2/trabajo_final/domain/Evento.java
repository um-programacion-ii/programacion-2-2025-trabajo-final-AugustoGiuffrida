package com.um.programacion2.trabajo_final.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad para almacenar localmente la información de los eventos
 * sincronizados desde el servicio de la Cátedra.
 */
@Entity
@Table(name = "evento")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Evento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "evento_id_catedra", nullable = false, unique = true)
    private Long eventoIdCatedra;

    @NotNull
    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "resumen")
    private String resumen;

    @Column(name = "descripcion")
    private String descripcion;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private Instant fecha;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "imagen")
    private String imagen;

    @NotNull
    @Column(name = "fila_asientos", nullable = false)
    private Integer filaAsientos;

    @NotNull
    @Column(name = "column_asientos", nullable = false)
    private Integer columnAsientos;

    @NotNull
    @Column(name = "precio_entrada", precision = 21, scale = 2, nullable = false)
    private BigDecimal precioEntrada;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "evento")
    @JsonIgnoreProperties(value = { "evento", "user", "asientos" }, allowSetters = true)
    private Set<Venta> ventas = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Set<Venta> getVentas() {
        return this.ventas;
    }

    public Evento ventas(Set<Venta> ventas) {
        this.setVentas(ventas);
        return this;
    }

    public Evento addVenta(Venta venta) {
        this.ventas.add(venta);
        venta.setEvento(this);
        return this;
    }

    public Evento removeVenta(Venta venta) {
        this.ventas.remove(venta);
        venta.setEvento(null);
        return this;
    }

    public void setVentas(Set<Venta> ventas) {
        if (this.ventas != null) {
            this.ventas.forEach(i -> i.setEvento(null));
        }
        if (ventas != null) {
            ventas.forEach(i -> i.setEvento(this));
        }
        this.ventas = ventas;
    }
    public Long getId() {
        return this.id;
    }

    public Evento id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEventoIdCatedra() {
        return this.eventoIdCatedra;
    }

    public Evento eventoIdCatedra(Long eventoIdCatedra) {
        this.setEventoIdCatedra(eventoIdCatedra);
        return this;
    }

    public void setEventoIdCatedra(Long eventoIdCatedra) {
        this.eventoIdCatedra = eventoIdCatedra;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public Evento titulo(String titulo) {
        this.setTitulo(titulo);
        return this;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getResumen() {
        return this.resumen;
    }

    public Evento resumen(String resumen) {
        this.setResumen(resumen);
        return this;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Evento descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Instant getFecha() {
        return this.fecha;
    }

    public Evento fecha(Instant fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public Evento direccion(String direccion) {
        this.setDireccion(direccion);
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getImagen() {
        return this.imagen;
    }

    public Evento imagen(String imagen) {
        this.setImagen(imagen);
        return this;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Integer getFilaAsientos() {
        return this.filaAsientos;
    }

    public Evento filaAsientos(Integer filaAsientos) {
        this.setFilaAsientos(filaAsientos);
        return this;
    }

    public void setFilaAsientos(Integer filaAsientos) {
        this.filaAsientos = filaAsientos;
    }

    public Integer getColumnAsientos() {
        return this.columnAsientos;
    }

    public Evento columnAsientos(Integer columnAsientos) {
        this.setColumnAsientos(columnAsientos);
        return this;
    }

    public void setColumnAsientos(Integer columnAsientos) {
        this.columnAsientos = columnAsientos;
    }

    public BigDecimal getPrecioEntrada() {
        return this.precioEntrada;
    }

    public Evento precioEntrada(BigDecimal precioEntrada) {
        this.setPrecioEntrada(precioEntrada);
        return this;
    }

    public void setPrecioEntrada(BigDecimal precioEntrada) {
        this.precioEntrada = precioEntrada;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Evento)) {
            return false;
        }
        return getId() != null && getId().equals(((Evento) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Evento{" +
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
