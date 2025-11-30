package com.um.programacion2.trabajo_final.service.dto.proxy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AsientoRedisDTO implements Serializable {
    private int fila;
    private int columna;
    private String estado;
    private String expira;

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
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getExpira() {
        return expira;
    }
    public void setExpira(String expira) {
        this.expira = expira;
    }
}
