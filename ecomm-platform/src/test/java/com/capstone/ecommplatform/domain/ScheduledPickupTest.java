package com.capstone.ecommplatform.domain;

import static com.capstone.ecommplatform.domain.ScheduledPickupTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.capstone.ecommplatform.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ScheduledPickupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ScheduledPickup.class);
        ScheduledPickup scheduledPickup1 = getScheduledPickupSample1();
        ScheduledPickup scheduledPickup2 = new ScheduledPickup();
        assertThat(scheduledPickup1).isNotEqualTo(scheduledPickup2);

        scheduledPickup2.setId(scheduledPickup1.getId());
        assertThat(scheduledPickup1).isEqualTo(scheduledPickup2);

        scheduledPickup2 = getScheduledPickupSample2();
        assertThat(scheduledPickup1).isNotEqualTo(scheduledPickup2);
    }
}
