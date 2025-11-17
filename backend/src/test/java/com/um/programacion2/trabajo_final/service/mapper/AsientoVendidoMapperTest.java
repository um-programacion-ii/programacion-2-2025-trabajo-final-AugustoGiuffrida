package com.um.programacion2.trabajo_final.service.mapper;

import static com.um.programacion2.trabajo_final.domain.AsientoVendidoAsserts.*;
import static com.um.programacion2.trabajo_final.domain.AsientoVendidoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AsientoVendidoMapperTest {

    private AsientoVendidoMapper asientoVendidoMapper;

    @BeforeEach
    void setUp() {
        asientoVendidoMapper = new AsientoVendidoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAsientoVendidoSample1();
        var actual = asientoVendidoMapper.toEntity(asientoVendidoMapper.toDto(expected));
        assertAsientoVendidoAllPropertiesEquals(expected, actual);
    }
}
