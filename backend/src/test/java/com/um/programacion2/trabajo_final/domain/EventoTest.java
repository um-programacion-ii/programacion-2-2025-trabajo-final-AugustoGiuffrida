package com.um.programacion2.trabajo_final.domain;

import static com.um.programacion2.trabajo_final.domain.EventoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.um.programacion2.trabajo_final.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Evento.class);
        Evento evento1 = getEventoSample1();
        Evento evento2 = new Evento();
        assertThat(evento1).isNotEqualTo(evento2);

        evento2.setId(evento1.getId());
        assertThat(evento1).isEqualTo(evento2);

        evento2 = getEventoSample2();
        assertThat(evento1).isNotEqualTo(evento2);
    }
}
