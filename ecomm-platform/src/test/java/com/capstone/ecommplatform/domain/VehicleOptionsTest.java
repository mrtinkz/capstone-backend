package com.capstone.ecommplatform.domain;

import static com.capstone.ecommplatform.domain.VehicleOptionsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.capstone.ecommplatform.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VehicleOptionsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehicleOptions.class);
        VehicleOptions vehicleOptions1 = getVehicleOptionsSample1();
        VehicleOptions vehicleOptions2 = new VehicleOptions();
        assertThat(vehicleOptions1).isNotEqualTo(vehicleOptions2);

        vehicleOptions2.setId(vehicleOptions1.getId());
        assertThat(vehicleOptions1).isEqualTo(vehicleOptions2);

        vehicleOptions2 = getVehicleOptionsSample2();
        assertThat(vehicleOptions1).isNotEqualTo(vehicleOptions2);
    }
}
