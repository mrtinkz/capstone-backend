package com.capstone.ecommplatform.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ScheduledPickupTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ScheduledPickup getScheduledPickupSample1() {
        return new ScheduledPickup().id(1L).shopperId(1L).orderId(1L);
    }

    public static ScheduledPickup getScheduledPickupSample2() {
        return new ScheduledPickup().id(2L).shopperId(2L).orderId(2L);
    }

    public static ScheduledPickup getScheduledPickupRandomSampleGenerator() {
        return new ScheduledPickup()
            .id(longCount.incrementAndGet())
            .shopperId(longCount.incrementAndGet())
            .orderId(longCount.incrementAndGet());
    }
}
