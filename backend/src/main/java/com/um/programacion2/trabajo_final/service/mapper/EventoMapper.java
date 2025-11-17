package com.um.programacion2.trabajo_final.service.mapper;

import com.um.programacion2.trabajo_final.service.dto.catedra.EventoCatedraDTO;
import org.mapstruct.MappingTarget;
import com.um.programacion2.trabajo_final.domain.Evento;
import com.um.programacion2.trabajo_final.service.dto.EventoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Evento} and its DTO {@link EventoDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventoMapper extends EntityMapper<EventoDTO, Evento> {

    /**
     * Convierte un DTO de Cátedra a nuestra entidad Evento (para creación).
     */
    @Mapping(source = "id", target = "eventoIdCatedra") // dto.id -> evento.eventoIdCatedra
    @Mapping(target = "id", ignore = true) // Ignoramos el ID de nuestra BD local
    @Mapping(target = "ventas", ignore = true) // Ignoramos la relación
    Evento eventoFromCatedraDTO(EventoCatedraDTO dto);

    /**
     * Actualiza una entidad Evento existente con datos de un DTO de Cátedra.
     */
    @Mapping(source = "id", target = "eventoIdCatedra")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ventas", ignore = true)
    void updateEventoFromCatedraDTO(EventoCatedraDTO dto, @MappingTarget Evento evento);
}
