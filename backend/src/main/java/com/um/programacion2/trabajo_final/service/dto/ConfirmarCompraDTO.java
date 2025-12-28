package com.um.programacion2.trabajo_final.service.dto;

import java.util.List;

public class ConfirmarCompraDTO {
    // Lista de nombres. Debe coincidir en orden o cantidad con los asientos de la sesión.
    // O simplificamos: enviamos un objeto {fila, columna, nombre} para asegurar correspondencia.
    private List<DetalleAsientoCompra> detalles;

    public List<DetalleAsientoCompra> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleAsientoCompra> detalles) {
        this.detalles = detalles;
    }
}
