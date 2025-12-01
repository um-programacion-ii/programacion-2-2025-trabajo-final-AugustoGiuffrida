package com.um.programacion2.trabajo_final.service.dto;

import java.io.Serializable;

public class AsientoSesionDTO implements Serializable {
    private int fila;
    private int columna;

    public AsientoSesionDTO() {
    }

    public AsientoSesionDTO(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }
}
