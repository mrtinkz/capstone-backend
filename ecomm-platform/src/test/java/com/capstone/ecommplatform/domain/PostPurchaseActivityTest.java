package com.capstone.ecommplatform.domain;

import static com.capstone.ecommplatform.domain.PostPurchaseActivityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.capstone.ecommplatform.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PostPurchaseActivityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PostPurchaseActivity.class);
        PostPurchaseActivity postPurchaseActivity1 = getPostPurchaseActivitySample1();
        PostPurchaseActivity postPurchaseActivity2 = new PostPurchaseActivity();
        assertThat(postPurchaseActivity1).isNotEqualTo(postPurchaseActivity2);

        postPurchaseActivity2.setId(postPurchaseActivity1.getId());
        assertThat(postPurchaseActivity1).isEqualTo(postPurchaseActivity2);

        postPurchaseActivity2 = getPostPurchaseActivitySample2();
        assertThat(postPurchaseActivity1).isNotEqualTo(postPurchaseActivity2);
    }
}
