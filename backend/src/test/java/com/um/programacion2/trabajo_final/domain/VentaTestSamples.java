package com.um.programacion2.trabajo_final.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class VentaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Venta getVentaSample1() {
        return new Venta().id(1L).ventaIdCatedra(1L).descripcion("descripcion1");
    }

    public static Venta getVentaSample2() {
        return new Venta().id(2L).ventaIdCatedra(2L).descripcion("descripcion2");
    }

    public static Venta getVentaRandomSampleGenerator() {
        return new Venta()
            .id(longCount.incrementAndGet())
            .ventaIdCatedra(longCount.incrementAndGet())
            .descripcion(UUID.randomUUID().toString());
    }
}
