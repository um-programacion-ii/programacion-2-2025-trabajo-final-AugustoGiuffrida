package com.um.programacion2.trabajo_final.service.dto;

import java.util.List;

public class ConfirmarCompraDTO {
    // Lista de nombres. Debe coincidir en orden o cantidad con los asientos de la sesión.
    // O simplificamos: enviamos un objeto {fila, columna, nombre} para asegurar correspondencia.
    private List<DetalleAsientoCompra> detalles;

    public List<DetalleAsientoCompra> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleAsientoCompra> detalles) { this.detalles = detalles; }

    public static class DetalleAsientoCompra {
        private int fila;
        private int columna;
        private String nombrePersona;

        public int getColumna() {
            return columna;
        }

        public void setColumna(int columna) {
            this.columna = columna;
        }

        public String getNombrePersona() {
            return nombrePersona;
        }

        public void setNombrePersona(String nombrePersona) {
            this.nombrePersona = nombrePersona;
        }

        public int getFila() {
            return fila;
        }

        public void setFila(int fila) {
            this.fila = fila;
        }
    }
}
