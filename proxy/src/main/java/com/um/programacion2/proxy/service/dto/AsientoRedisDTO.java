package com.um.programacion2.proxy.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AsientoRedisDTO {
    private int fila;
    private int columna;
    private String estado; // "Bloqueado" o "Vendido"
    private String expira;
}