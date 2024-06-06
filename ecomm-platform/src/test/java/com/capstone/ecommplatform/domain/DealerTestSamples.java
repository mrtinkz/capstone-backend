package com.capstone.ecommplatform.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DealerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Dealer getDealerSample1() {
        return new Dealer().id(1L).name("name1").address("address1").city("city1").state("state1").zipCode("zipCode1").website("website1");
    }

    public static Dealer getDealerSample2() {
        return new Dealer().id(2L).name("name2").address("address2").city("city2").state("state2").zipCode("zipCode2").website("website2");
    }

    public static Dealer getDealerRandomSampleGenerator() {
        return new Dealer()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .city(UUID.randomUUID().toString())
            .state(UUID.randomUUID().toString())
            .zipCode(UUID.randomUUID().toString())
            .website(UUID.randomUUID().toString());
    }
}
