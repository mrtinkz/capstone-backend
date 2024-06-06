package com.capstone.ecommplatform.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DocumentBlobTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DocumentBlob getDocumentBlobSample1() {
        return new DocumentBlob().id(1L).postPurchaseActivityId(1L).name("name1").mimeType("mimeType1");
    }

    public static DocumentBlob getDocumentBlobSample2() {
        return new DocumentBlob().id(2L).postPurchaseActivityId(2L).name("name2").mimeType("mimeType2");
    }

    public static DocumentBlob getDocumentBlobRandomSampleGenerator() {
        return new DocumentBlob()
            .id(longCount.incrementAndGet())
            .postPurchaseActivityId(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .mimeType(UUID.randomUUID().toString());
    }
}
