package com.capstone.ecommplatform.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PostPurchaseActivityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PostPurchaseActivity getPostPurchaseActivitySample1() {
        return new PostPurchaseActivity().id(1L).orderId(1L);
    }

    public static PostPurchaseActivity getPostPurchaseActivitySample2() {
        return new PostPurchaseActivity().id(2L).orderId(2L);
    }

    public static PostPurchaseActivity getPostPurchaseActivityRandomSampleGenerator() {
        return new PostPurchaseActivity().id(longCount.incrementAndGet()).orderId(longCount.incrementAndGet());
    }
}
