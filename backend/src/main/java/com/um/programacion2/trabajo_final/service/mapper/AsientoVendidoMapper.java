package com.um.programacion2.trabajo_final.service.mapper;

import com.um.programacion2.trabajo_final.domain.AsientoVendido;
import com.um.programacion2.trabajo_final.domain.Venta;
import com.um.programacion2.trabajo_final.service.dto.AsientoVendidoDTO;
import com.um.programacion2.trabajo_final.service.dto.VentaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AsientoVendido} and its DTO {@link AsientoVendidoDTO}.
 */
@Mapper(componentModel = "spring")
public interface AsientoVendidoMapper extends EntityMapper<AsientoVendidoDTO, AsientoVendido> {
    @Mapping(target = "venta", source = "venta",qualifiedByName = "ventaId")
    AsientoVendidoDTO toDto(AsientoVendido s);

    @Named("ventaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VentaDTO toDtoVentaId(Venta venta);
}
