package com.um.programacion2.trabajo_final.service.dto.catedra;

public class AsientoVentaCatedraDTO {
    private int fila;
    private int columna;
    private String persona;

    public AsientoVentaCatedraDTO() {}
    public AsientoVentaCatedraDTO(int fila, int columna, String persona) {
        this.fila = fila;
        this.columna = columna;
        this.persona = persona;
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
    public String getPersona() {
        return persona;
    }
    public void setPersona(String persona) {
        this.persona = persona;
    }
}
