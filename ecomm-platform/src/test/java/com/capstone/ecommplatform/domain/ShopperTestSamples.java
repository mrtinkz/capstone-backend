package com.capstone.ecommplatform.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ShopperTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Shopper getShopperSample1() {
        return new Shopper().id(1L).userId(1L).firstName("firstName1").lastName("lastName1").email("email1");
    }

    public static Shopper getShopperSample2() {
        return new Shopper().id(2L).userId(2L).firstName("firstName2").lastName("lastName2").email("email2");
    }

    public static Shopper getShopperRandomSampleGenerator() {
        return new Shopper()
            .id(longCount.incrementAndGet())
            .userId(longCount.incrementAndGet())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString());
    }
}
