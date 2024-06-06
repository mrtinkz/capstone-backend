package com.capstone.ecommplatform.domain;

import static com.capstone.ecommplatform.domain.PricingSummaryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.capstone.ecommplatform.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PricingSummaryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PricingSummary.class);
        PricingSummary pricingSummary1 = getPricingSummarySample1();
        PricingSummary pricingSummary2 = new PricingSummary();
        assertThat(pricingSummary1).isNotEqualTo(pricingSummary2);

        pricingSummary2.setId(pricingSummary1.getId());
        assertThat(pricingSummary1).isEqualTo(pricingSummary2);

        pricingSummary2 = getPricingSummarySample2();
        assertThat(pricingSummary1).isNotEqualTo(pricingSummary2);
    }
}
