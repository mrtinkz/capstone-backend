package com.capstone.ecommplatform.domain;

import static com.capstone.ecommplatform.domain.PaymentDetailTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.capstone.ecommplatform.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentDetailTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentDetail.class);
        PaymentDetail paymentDetail1 = getPaymentDetailSample1();
        PaymentDetail paymentDetail2 = new PaymentDetail();
        assertThat(paymentDetail1).isNotEqualTo(paymentDetail2);

        paymentDetail2.setId(paymentDetail1.getId());
        assertThat(paymentDetail1).isEqualTo(paymentDetail2);

        paymentDetail2 = getPaymentDetailSample2();
        assertThat(paymentDetail1).isNotEqualTo(paymentDetail2);
    }
}
