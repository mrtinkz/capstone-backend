package com.capstone.ecommplatform.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class VehicleOptionsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static VehicleOptions getVehicleOptionsSample1() {
        return new VehicleOptions().id(1L).estimatedMileage(1);
    }

    public static VehicleOptions getVehicleOptionsSample2() {
        return new VehicleOptions().id(2L).estimatedMileage(2);
    }

    public static VehicleOptions getVehicleOptionsRandomSampleGenerator() {
        return new VehicleOptions().id(longCount.incrementAndGet()).estimatedMileage(intCount.incrementAndGet());
    }
}
