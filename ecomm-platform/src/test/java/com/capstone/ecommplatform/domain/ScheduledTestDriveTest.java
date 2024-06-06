package com.capstone.ecommplatform.domain;

import static com.capstone.ecommplatform.domain.ScheduledTestDriveTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.capstone.ecommplatform.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ScheduledTestDriveTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ScheduledTestDrive.class);
        ScheduledTestDrive scheduledTestDrive1 = getScheduledTestDriveSample1();
        ScheduledTestDrive scheduledTestDrive2 = new ScheduledTestDrive();
        assertThat(scheduledTestDrive1).isNotEqualTo(scheduledTestDrive2);

        scheduledTestDrive2.setId(scheduledTestDrive1.getId());
        assertThat(scheduledTestDrive1).isEqualTo(scheduledTestDrive2);

        scheduledTestDrive2 = getScheduledTestDriveSample2();
        assertThat(scheduledTestDrive1).isNotEqualTo(scheduledTestDrive2);
    }
}
