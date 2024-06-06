package com.capstone.ecommplatform.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class FinancingTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Financing getFinancingSample1() {
        return new Financing().id(1L).provider("provider1").loanTerm(1).orderId(1L);
    }

    public static Financing getFinancingSample2() {
        return new Financing().id(2L).provider("provider2").loanTerm(2).orderId(2L);
    }

    public static Financing getFinancingRandomSampleGenerator() {
        return new Financing()
            .id(longCount.incrementAndGet())
            .provider(UUID.randomUUID().toString())
            .loanTerm(intCount.incrementAndGet())
            .orderId(longCount.incrementAndGet());
    }
}
