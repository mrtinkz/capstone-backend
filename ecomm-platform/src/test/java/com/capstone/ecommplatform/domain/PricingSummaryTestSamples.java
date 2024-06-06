package com.capstone.ecommplatform.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PricingSummaryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PricingSummary getPricingSummarySample1() {
        return new PricingSummary().id(1L);
    }

    public static PricingSummary getPricingSummarySample2() {
        return new PricingSummary().id(2L);
    }

    public static PricingSummary getPricingSummaryRandomSampleGenerator() {
        return new PricingSummary().id(longCount.incrementAndGet());
    }
}
