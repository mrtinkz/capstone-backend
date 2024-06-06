package com.capstone.ecommplatform.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PaymentDetailTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PaymentDetail getPaymentDetailSample1() {
        return new PaymentDetail().id(1L).orderId(1L).cardNumber("cardNumber1").cardHolderName("cardHolderName1");
    }

    public static PaymentDetail getPaymentDetailSample2() {
        return new PaymentDetail().id(2L).orderId(2L).cardNumber("cardNumber2").cardHolderName("cardHolderName2");
    }

    public static PaymentDetail getPaymentDetailRandomSampleGenerator() {
        return new PaymentDetail()
            .id(longCount.incrementAndGet())
            .orderId(longCount.incrementAndGet())
            .cardNumber(UUID.randomUUID().toString())
            .cardHolderName(UUID.randomUUID().toString());
    }
}
