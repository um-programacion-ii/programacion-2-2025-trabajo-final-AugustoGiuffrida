package com.um.programacion2.trabajo_final.service.mapper;

import com.um.programacion2.trabajo_final.domain.Evento;
import com.um.programacion2.trabajo_final.domain.User;
import com.um.programacion2.trabajo_final.domain.Venta;
import com.um.programacion2.trabajo_final.service.dto.EventoDTO;
import com.um.programacion2.trabajo_final.service.dto.UserDTO;
import com.um.programacion2.trabajo_final.service.dto.VentaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Venta} and its DTO {@link VentaDTO}.
 */
@Mapper(componentModel = "spring")
public interface VentaMapper extends EntityMapper<VentaDTO, Venta> {
    @Mapping(target = "evento", source = "evento", qualifiedByName = "eventoCompleto")
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    VentaDTO toDto(Venta s);

    @Named("eventoCompleto")
    EventoDTO toDtoEventoCompleto(Evento evento);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
