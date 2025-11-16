package com.um.programacion2.trabajo_final.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.um.programacion2.trabajo_final.domain.enumeration.EstadoVenta;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad para registrar las ventas realizadas.
 */
@Entity
@Table(name = "venta")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Venta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "venta_id_catedra")
    private Long ventaIdCatedra;

    @NotNull
    @Column(name = "fecha_venta", nullable = false)
    private Instant fechaVenta;

    @NotNull
    @Column(name = "precio_venta", precision = 21, scale = 2, nullable = false)
    private BigDecimal precioVenta;

    @NotNull
    @Column(name = "resultado", nullable = false)
    private Boolean resultado;

    @Column(name = "descripcion")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_venta")
    private EstadoVenta estadoVenta;

    /**
     * Una Venta puede tener muchos Asientos Vendidos.
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "venta")
    @JsonIgnoreProperties(value = { "venta" }, allowSetters = true)
    private Set<AsientoVendido> asientos = new HashSet<>();

    /**
     * Una Venta pertenece a un solo Evento.
     */
    @ManyToOne(optional = false)
    @NotNull
    private Evento evento;

    /**
     * Una Venta es realizada por un solo Usuario (User).
     * El User ya existe en JHipster.
     */
    @ManyToOne(optional = false)
    @NotNull
    private User user;
    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Venta id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVentaIdCatedra() {
        return this.ventaIdCatedra;
    }

    public Venta ventaIdCatedra(Long ventaIdCatedra) {
        this.setVentaIdCatedra(ventaIdCatedra);
        return this;
    }

    public void setVentaIdCatedra(Long ventaIdCatedra) {
        this.ventaIdCatedra = ventaIdCatedra;
    }

    public Instant getFechaVenta() {
        return this.fechaVenta;
    }

    public Venta fechaVenta(Instant fechaVenta) {
        this.setFechaVenta(fechaVenta);
        return this;
    }

    public void setFechaVenta(Instant fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public BigDecimal getPrecioVenta() {
        return this.precioVenta;
    }

    public Venta precioVenta(BigDecimal precioVenta) {
        this.setPrecioVenta(precioVenta);
        return this;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Boolean getResultado() {
        return this.resultado;
    }

    public Venta resultado(Boolean resultado) {
        this.setResultado(resultado);
        return this;
    }

    public void setResultado(Boolean resultado) {
        this.resultado = resultado;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Venta descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public EstadoVenta getEstadoVenta() {
        return this.estadoVenta;
    }

    public Venta estadoVenta(EstadoVenta estadoVenta) {
        this.setEstadoVenta(estadoVenta);
        return this;
    }

    public void setEstadoVenta(EstadoVenta estadoVenta) {
        this.estadoVenta = estadoVenta;
    }

    public Set<AsientoVendido> getAsientos() {
        return this.asientos;
    }

    public void setAsientos(Set<AsientoVendido> asientoVendidos) {
        if (this.asientos != null) {
            this.asientos.forEach(i -> i.setVenta(null));
        }
        if (asientoVendidos != null) {
            asientoVendidos.forEach(i -> i.setVenta(this));
        }
        this.asientos = asientoVendidos;
    }

    public Venta asientos(Set<AsientoVendido> asientoVendidos) {
        this.setAsientos(asientoVendidos);
        return this;
    }

    public Venta addAsientos(AsientoVendido asientoVendido) {
        this.asientos.add(asientoVendido);
        asientoVendido.setVenta(this);
        return this;
    }

    public Venta removeAsientos(AsientoVendido asientoVendido) {
        this.asientos.remove(asientoVendido);
        asientoVendido.setVenta(null);
        return this;
    }

    public Evento getEvento() {
        return this.evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Venta evento(Evento evento) {
        this.setEvento(evento);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Venta user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Venta)) {
            return false;
        }
        return getId() != null && getId().equals(((Venta) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Venta{" +
            "id=" + getId() +
            ", ventaIdCatedra=" + getVentaIdCatedra() +
            ", fechaVenta='" + getFechaVenta() + "'" +
            ", precioVenta=" + getPrecioVenta() +
            ", resultado='" + getResultado() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", estadoVenta='" + getEstadoVenta() + "'" +
            "}";
    }
}
