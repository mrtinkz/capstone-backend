package com.capstone.ecommplatform.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class OrderTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Order getOrderSample1() {
        return new Order().id(1L).shopperId(1L).vehicleId(1L).dealerId(1L);
    }

    public static Order getOrderSample2() {
        return new Order().id(2L).shopperId(2L).vehicleId(2L).dealerId(2L);
    }

    public static Order getOrderRandomSampleGenerator() {
        return new Order()
            .id(longCount.incrementAndGet())
            .shopperId(longCount.incrementAndGet())
            .vehicleId(longCount.incrementAndGet())
            .dealerId(longCount.incrementAndGet());
    }
}
