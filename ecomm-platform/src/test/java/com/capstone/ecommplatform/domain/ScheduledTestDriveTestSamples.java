package com.capstone.ecommplatform.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ScheduledTestDriveTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ScheduledTestDrive getScheduledTestDriveSample1() {
        return new ScheduledTestDrive().id(1L).shopperId(1L);
    }

    public static ScheduledTestDrive getScheduledTestDriveSample2() {
        return new ScheduledTestDrive().id(2L).shopperId(2L);
    }

    public static ScheduledTestDrive getScheduledTestDriveRandomSampleGenerator() {
        return new ScheduledTestDrive().id(longCount.incrementAndGet()).shopperId(longCount.incrementAndGet());
    }
}
