package com.capstone.ecommplatform.domain;

import static com.capstone.ecommplatform.domain.FinancingTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.capstone.ecommplatform.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FinancingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Financing.class);
        Financing financing1 = getFinancingSample1();
        Financing financing2 = new Financing();
        assertThat(financing1).isNotEqualTo(financing2);

        financing2.setId(financing1.getId());
        assertThat(financing1).isEqualTo(financing2);

        financing2 = getFinancingSample2();
        assertThat(financing1).isNotEqualTo(financing2);
    }
}
