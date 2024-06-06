package com.capstone.ecommplatform.domain;

import static com.capstone.ecommplatform.domain.ShopperTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.capstone.ecommplatform.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShopperTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Shopper.class);
        Shopper shopper1 = getShopperSample1();
        Shopper shopper2 = new Shopper();
        assertThat(shopper1).isNotEqualTo(shopper2);

        shopper2.setId(shopper1.getId());
        assertThat(shopper1).isEqualTo(shopper2);

        shopper2 = getShopperSample2();
        assertThat(shopper1).isNotEqualTo(shopper2);
    }
}
