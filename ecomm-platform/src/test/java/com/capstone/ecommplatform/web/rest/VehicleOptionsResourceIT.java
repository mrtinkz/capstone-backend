package com.capstone.ecommplatform.web.rest;

import static com.capstone.ecommplatform.domain.VehicleOptionsAsserts.*;
import static com.capstone.ecommplatform.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.capstone.ecommplatform.IntegrationTest;
import com.capstone.ecommplatform.domain.VehicleOptions;
import com.capstone.ecommplatform.domain.enumeration.Color;
import com.capstone.ecommplatform.domain.enumeration.DrivetrainType;
import com.capstone.ecommplatform.domain.enumeration.EngineType;
import com.capstone.ecommplatform.domain.enumeration.TransmissionType;
import com.capstone.ecommplatform.domain.enumeration.Trim;
import com.capstone.ecommplatform.repository.VehicleOptionsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link VehicleOptionsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VehicleOptionsResourceIT {

    private static final Integer DEFAULT_ESTIMATED_MILEAGE = 1;
    private static final Integer UPDATED_ESTIMATED_MILEAGE = 2;

    private static final EngineType DEFAULT_ENGINE = EngineType.GASOLINE;
    private static final EngineType UPDATED_ENGINE = EngineType.DIESEL;

    private static final DrivetrainType DEFAULT_DRIVETRAIN = DrivetrainType.FRONT_WHEEL_DRIVE;
    private static final DrivetrainType UPDATED_DRIVETRAIN = DrivetrainType.REAR_WHEEL_DRIVE;

    private static final TransmissionType DEFAULT_TRANSMISSION = TransmissionType.MANUAL;
    private static final TransmissionType UPDATED_TRANSMISSION = TransmissionType.AUTOMATIC;

    private static final Trim DEFAULT_TRIM = Trim.TRIM_ONE;
    private static final Trim UPDATED_TRIM = Trim.TRIM_TWO;

    private static final Color DEFAULT_COLOR = Color.BLACK;
    private static final Color UPDATED_COLOR = Color.BROWN;

    private static final String ENTITY_API_URL = "/api/vehicle-options";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VehicleOptionsRepository vehicleOptionsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVehicleOptionsMockMvc;

    private VehicleOptions vehicleOptions;

    private VehicleOptions insertedVehicleOptions;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleOptions createEntity(EntityManager em) {
        VehicleOptions vehicleOptions = new VehicleOptions()
            .estimatedMileage(DEFAULT_ESTIMATED_MILEAGE)
            .engine(DEFAULT_ENGINE)
            .drivetrain(DEFAULT_DRIVETRAIN)
            .transmission(DEFAULT_TRANSMISSION)
            .trim(DEFAULT_TRIM)
            .color(DEFAULT_COLOR);
        return vehicleOptions;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleOptions createUpdatedEntity(EntityManager em) {
        VehicleOptions vehicleOptions = new VehicleOptions()
            .estimatedMileage(UPDATED_ESTIMATED_MILEAGE)
            .engine(UPDATED_ENGINE)
            .drivetrain(UPDATED_DRIVETRAIN)
            .transmission(UPDATED_TRANSMISSION)
            .trim(UPDATED_TRIM)
            .color(UPDATED_COLOR);
        return vehicleOptions;
    }

    @BeforeEach
    public void initTest() {
        vehicleOptions = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedVehicleOptions != null) {
            vehicleOptionsRepository.delete(insertedVehicleOptions);
            insertedVehicleOptions = null;
        }
    }

    @Test
    @Transactional
    void createVehicleOptions() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the VehicleOptions
        var returnedVehicleOptions = om.readValue(
            restVehicleOptionsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleOptions)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VehicleOptions.class
        );

        // Validate the VehicleOptions in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertVehicleOptionsUpdatableFieldsEquals(returnedVehicleOptions, getPersistedVehicleOptions(returnedVehicleOptions));

        insertedVehicleOptions = returnedVehicleOptions;
    }

    @Test
    @Transactional
    void createVehicleOptionsWithExistingId() throws Exception {
        // Create the VehicleOptions with an existing ID
        vehicleOptions.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVehicleOptionsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleOptions)))
            .andExpect(status().isBadRequest());

        // Validate the VehicleOptions in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVehicleOptions() throws Exception {
        // Initialize the database
        insertedVehicleOptions = vehicleOptionsRepository.saveAndFlush(vehicleOptions);

        // Get all the vehicleOptionsList
        restVehicleOptionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicleOptions.getId().intValue())))
            .andExpect(jsonPath("$.[*].estimatedMileage").value(hasItem(DEFAULT_ESTIMATED_MILEAGE)))
            .andExpect(jsonPath("$.[*].engine").value(hasItem(DEFAULT_ENGINE.toString())))
            .andExpect(jsonPath("$.[*].drivetrain").value(hasItem(DEFAULT_DRIVETRAIN.toString())))
            .andExpect(jsonPath("$.[*].transmission").value(hasItem(DEFAULT_TRANSMISSION.toString())))
            .andExpect(jsonPath("$.[*].trim").value(hasItem(DEFAULT_TRIM.toString())))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR.toString())));
    }

    @Test
    @Transactional
    void getVehicleOptions() throws Exception {
        // Initialize the database
        insertedVehicleOptions = vehicleOptionsRepository.saveAndFlush(vehicleOptions);

        // Get the vehicleOptions
        restVehicleOptionsMockMvc
            .perform(get(ENTITY_API_URL_ID, vehicleOptions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vehicleOptions.getId().intValue()))
            .andExpect(jsonPath("$.estimatedMileage").value(DEFAULT_ESTIMATED_MILEAGE))
            .andExpect(jsonPath("$.engine").value(DEFAULT_ENGINE.toString()))
            .andExpect(jsonPath("$.drivetrain").value(DEFAULT_DRIVETRAIN.toString()))
            .andExpect(jsonPath("$.transmission").value(DEFAULT_TRANSMISSION.toString()))
            .andExpect(jsonPath("$.trim").value(DEFAULT_TRIM.toString()))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR.toString()));
    }

    @Test
    @Transactional
    void getNonExistingVehicleOptions() throws Exception {
        // Get the vehicleOptions
        restVehicleOptionsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVehicleOptions() throws Exception {
        // Initialize the database
        insertedVehicleOptions = vehicleOptionsRepository.saveAndFlush(vehicleOptions);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleOptions
        VehicleOptions updatedVehicleOptions = vehicleOptionsRepository.findById(vehicleOptions.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVehicleOptions are not directly saved in db
        em.detach(updatedVehicleOptions);
        updatedVehicleOptions
            .estimatedMileage(UPDATED_ESTIMATED_MILEAGE)
            .engine(UPDATED_ENGINE)
            .drivetrain(UPDATED_DRIVETRAIN)
            .transmission(UPDATED_TRANSMISSION)
            .trim(UPDATED_TRIM)
            .color(UPDATED_COLOR);

        restVehicleOptionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVehicleOptions.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedVehicleOptions))
            )
            .andExpect(status().isOk());

        // Validate the VehicleOptions in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVehicleOptionsToMatchAllProperties(updatedVehicleOptions);
    }

    @Test
    @Transactional
    void putNonExistingVehicleOptions() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleOptions.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleOptionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehicleOptions.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleOptions))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleOptions in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVehicleOptions() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleOptions.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleOptionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleOptions))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleOptions in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVehicleOptions() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleOptions.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleOptionsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleOptions)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VehicleOptions in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVehicleOptionsWithPatch() throws Exception {
        // Initialize the database
        insertedVehicleOptions = vehicleOptionsRepository.saveAndFlush(vehicleOptions);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleOptions using partial update
        VehicleOptions partialUpdatedVehicleOptions = new VehicleOptions();
        partialUpdatedVehicleOptions.setId(vehicleOptions.getId());

        partialUpdatedVehicleOptions.engine(UPDATED_ENGINE).drivetrain(UPDATED_DRIVETRAIN).color(UPDATED_COLOR);

        restVehicleOptionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicleOptions.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVehicleOptions))
            )
            .andExpect(status().isOk());

        // Validate the VehicleOptions in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVehicleOptionsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedVehicleOptions, vehicleOptions),
            getPersistedVehicleOptions(vehicleOptions)
        );
    }

    @Test
    @Transactional
    void fullUpdateVehicleOptionsWithPatch() throws Exception {
        // Initialize the database
        insertedVehicleOptions = vehicleOptionsRepository.saveAndFlush(vehicleOptions);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicleOptions using partial update
        VehicleOptions partialUpdatedVehicleOptions = new VehicleOptions();
        partialUpdatedVehicleOptions.setId(vehicleOptions.getId());

        partialUpdatedVehicleOptions
            .estimatedMileage(UPDATED_ESTIMATED_MILEAGE)
            .engine(UPDATED_ENGINE)
            .drivetrain(UPDATED_DRIVETRAIN)
            .transmission(UPDATED_TRANSMISSION)
            .trim(UPDATED_TRIM)
            .color(UPDATED_COLOR);

        restVehicleOptionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicleOptions.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVehicleOptions))
            )
            .andExpect(status().isOk());

        // Validate the VehicleOptions in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVehicleOptionsUpdatableFieldsEquals(partialUpdatedVehicleOptions, getPersistedVehicleOptions(partialUpdatedVehicleOptions));
    }

    @Test
    @Transactional
    void patchNonExistingVehicleOptions() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleOptions.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleOptionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vehicleOptions.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vehicleOptions))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleOptions in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVehicleOptions() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleOptions.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleOptionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vehicleOptions))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleOptions in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVehicleOptions() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleOptions.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleOptionsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(vehicleOptions)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VehicleOptions in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVehicleOptions() throws Exception {
        // Initialize the database
        insertedVehicleOptions = vehicleOptionsRepository.saveAndFlush(vehicleOptions);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the vehicleOptions
        restVehicleOptionsMockMvc
            .perform(delete(ENTITY_API_URL_ID, vehicleOptions.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return vehicleOptionsRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected VehicleOptions getPersistedVehicleOptions(VehicleOptions vehicleOptions) {
        return vehicleOptionsRepository.findById(vehicleOptions.getId()).orElseThrow();
    }

    protected void assertPersistedVehicleOptionsToMatchAllProperties(VehicleOptions expectedVehicleOptions) {
        assertVehicleOptionsAllPropertiesEquals(expectedVehicleOptions, getPersistedVehicleOptions(expectedVehicleOptions));
    }

    protected void assertPersistedVehicleOptionsToMatchUpdatableProperties(VehicleOptions expectedVehicleOptions) {
        assertVehicleOptionsAllUpdatablePropertiesEquals(expectedVehicleOptions, getPersistedVehicleOptions(expectedVehicleOptions));
    }
}
