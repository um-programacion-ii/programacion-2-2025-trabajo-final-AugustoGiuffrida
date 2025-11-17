package com.um.programacion2.trabajo_final.service.mapper;

import static com.um.programacion2.trabajo_final.domain.EventoAsserts.*;
import static com.um.programacion2.trabajo_final.domain.EventoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EventoMapperTest {

    private EventoMapper eventoMapper;

    @BeforeEach
    void setUp() {
        eventoMapper = new EventoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEventoSample1();
        var actual = eventoMapper.toEntity(eventoMapper.toDto(expected));
        assertEventoAllPropertiesEquals(expected, actual);
    }
}
