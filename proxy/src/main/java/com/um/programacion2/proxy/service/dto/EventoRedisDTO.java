package com.um.programacion2.proxy.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventoRedisDTO {
    private Long eventoId;
    private List<AsientoRedisDTO> asientos;
}