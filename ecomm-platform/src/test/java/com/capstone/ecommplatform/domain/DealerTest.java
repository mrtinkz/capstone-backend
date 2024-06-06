package com.capstone.ecommplatform.domain;

import static com.capstone.ecommplatform.domain.DealerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.capstone.ecommplatform.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DealerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dealer.class);
        Dealer dealer1 = getDealerSample1();
        Dealer dealer2 = new Dealer();
        assertThat(dealer1).isNotEqualTo(dealer2);

        dealer2.setId(dealer1.getId());
        assertThat(dealer1).isEqualTo(dealer2);

        dealer2 = getDealerSample2();
        assertThat(dealer1).isNotEqualTo(dealer2);
    }
}
