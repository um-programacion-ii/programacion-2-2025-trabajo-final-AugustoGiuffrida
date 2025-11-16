package com.um.programacion2.trabajo_final.domain;

import static com.um.programacion2.trabajo_final.domain.AsientoVendidoTestSamples.*;
import static com.um.programacion2.trabajo_final.domain.EventoTestSamples.*;
import static com.um.programacion2.trabajo_final.domain.VentaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.um.programacion2.trabajo_final.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class VentaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Venta.class);
        Venta venta1 = getVentaSample1();
        Venta venta2 = new Venta();
        assertThat(venta1).isNotEqualTo(venta2);

        venta2.setId(venta1.getId());
        assertThat(venta1).isEqualTo(venta2);

        venta2 = getVentaSample2();
        assertThat(venta1).isNotEqualTo(venta2);
    }

    @Test
    void asientosTest() {
        Venta venta = getVentaRandomSampleGenerator();
        AsientoVendido asientoVendidoBack = getAsientoVendidoRandomSampleGenerator();

        venta.addAsientos(asientoVendidoBack);
        assertThat(venta.getAsientos()).containsOnly(asientoVendidoBack);
        assertThat(asientoVendidoBack.getVenta()).isEqualTo(venta);

        venta.removeAsientos(asientoVendidoBack);
        assertThat(venta.getAsientos()).doesNotContain(asientoVendidoBack);
        assertThat(asientoVendidoBack.getVenta()).isNull();

        venta.asientos(new HashSet<>(Set.of(asientoVendidoBack)));
        assertThat(venta.getAsientos()).containsOnly(asientoVendidoBack);
        assertThat(asientoVendidoBack.getVenta()).isEqualTo(venta);

        venta.setAsientos(new HashSet<>());
        assertThat(venta.getAsientos()).doesNotContain(asientoVendidoBack);
        assertThat(asientoVendidoBack.getVenta()).isNull();
    }

    @Test
    void eventoTest() {
        Venta venta = getVentaRandomSampleGenerator();
        Evento eventoBack = getEventoRandomSampleGenerator();

        venta.setEvento(eventoBack);
        assertThat(venta.getEvento()).isEqualTo(eventoBack);

        venta.evento(null);
        assertThat(venta.getEvento()).isNull();
    }
}
