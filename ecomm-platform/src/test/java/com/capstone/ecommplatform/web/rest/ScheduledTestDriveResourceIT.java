package com.capstone.ecommplatform.web.rest;

import static com.capstone.ecommplatform.domain.ScheduledTestDriveAsserts.*;
import static com.capstone.ecommplatform.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.capstone.ecommplatform.IntegrationTest;
import com.capstone.ecommplatform.domain.ScheduledTestDrive;
import com.capstone.ecommplatform.repository.ScheduledTestDriveRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link ScheduledTestDriveResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ScheduledTestDriveResourceIT {

    private static final Long DEFAULT_SHOPPER_ID = 1L;
    private static final Long UPDATED_SHOPPER_ID = 2L;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/scheduled-test-drives";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ScheduledTestDriveRepository scheduledTestDriveRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restScheduledTestDriveMockMvc;

    private ScheduledTestDrive scheduledTestDrive;

    private ScheduledTestDrive insertedScheduledTestDrive;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ScheduledTestDrive createEntity(EntityManager em) {
        ScheduledTestDrive scheduledTestDrive = new ScheduledTestDrive().shopperId(DEFAULT_SHOPPER_ID).date(DEFAULT_DATE);
        return scheduledTestDrive;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ScheduledTestDrive createUpdatedEntity(EntityManager em) {
        ScheduledTestDrive scheduledTestDrive = new ScheduledTestDrive().shopperId(UPDATED_SHOPPER_ID).date(UPDATED_DATE);
        return scheduledTestDrive;
    }

    @BeforeEach
    public void initTest() {
        scheduledTestDrive = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedScheduledTestDrive != null) {
            scheduledTestDriveRepository.delete(insertedScheduledTestDrive);
            insertedScheduledTestDrive = null;
        }
    }

    @Test
    @Transactional
    void createScheduledTestDrive() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ScheduledTestDrive
        var returnedScheduledTestDrive = om.readValue(
            restScheduledTestDriveMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scheduledTestDrive)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ScheduledTestDrive.class
        );

        // Validate the ScheduledTestDrive in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertScheduledTestDriveUpdatableFieldsEquals(
            returnedScheduledTestDrive,
            getPersistedScheduledTestDrive(returnedScheduledTestDrive)
        );

        insertedScheduledTestDrive = returnedScheduledTestDrive;
    }

    @Test
    @Transactional
    void createScheduledTestDriveWithExistingId() throws Exception {
        // Create the ScheduledTestDrive with an existing ID
        scheduledTestDrive.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restScheduledTestDriveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scheduledTestDrive)))
            .andExpect(status().isBadRequest());

        // Validate the ScheduledTestDrive in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        scheduledTestDrive.setDate(null);

        // Create the ScheduledTestDrive, which fails.

        restScheduledTestDriveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scheduledTestDrive)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllScheduledTestDrives() throws Exception {
        // Initialize the database
        insertedScheduledTestDrive = scheduledTestDriveRepository.saveAndFlush(scheduledTestDrive);

        // Get all the scheduledTestDriveList
        restScheduledTestDriveMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scheduledTestDrive.getId().intValue())))
            .andExpect(jsonPath("$.[*].shopperId").value(hasItem(DEFAULT_SHOPPER_ID.intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    void getScheduledTestDrive() throws Exception {
        // Initialize the database
        insertedScheduledTestDrive = scheduledTestDriveRepository.saveAndFlush(scheduledTestDrive);

        // Get the scheduledTestDrive
        restScheduledTestDriveMockMvc
            .perform(get(ENTITY_API_URL_ID, scheduledTestDrive.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(scheduledTestDrive.getId().intValue()))
            .andExpect(jsonPath("$.shopperId").value(DEFAULT_SHOPPER_ID.intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingScheduledTestDrive() throws Exception {
        // Get the scheduledTestDrive
        restScheduledTestDriveMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingScheduledTestDrive() throws Exception {
        // Initialize the database
        insertedScheduledTestDrive = scheduledTestDriveRepository.saveAndFlush(scheduledTestDrive);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the scheduledTestDrive
        ScheduledTestDrive updatedScheduledTestDrive = scheduledTestDriveRepository.findById(scheduledTestDrive.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedScheduledTestDrive are not directly saved in db
        em.detach(updatedScheduledTestDrive);
        updatedScheduledTestDrive.shopperId(UPDATED_SHOPPER_ID).date(UPDATED_DATE);

        restScheduledTestDriveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedScheduledTestDrive.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedScheduledTestDrive))
            )
            .andExpect(status().isOk());

        // Validate the ScheduledTestDrive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedScheduledTestDriveToMatchAllProperties(updatedScheduledTestDrive);
    }

    @Test
    @Transactional
    void putNonExistingScheduledTestDrive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scheduledTestDrive.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScheduledTestDriveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, scheduledTestDrive.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(scheduledTestDrive))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduledTestDrive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchScheduledTestDrive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scheduledTestDrive.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduledTestDriveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(scheduledTestDrive))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduledTestDrive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamScheduledTestDrive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scheduledTestDrive.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduledTestDriveMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scheduledTestDrive)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ScheduledTestDrive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateScheduledTestDriveWithPatch() throws Exception {
        // Initialize the database
        insertedScheduledTestDrive = scheduledTestDriveRepository.saveAndFlush(scheduledTestDrive);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the scheduledTestDrive using partial update
        ScheduledTestDrive partialUpdatedScheduledTestDrive = new ScheduledTestDrive();
        partialUpdatedScheduledTestDrive.setId(scheduledTestDrive.getId());

        partialUpdatedScheduledTestDrive.shopperId(UPDATED_SHOPPER_ID).date(UPDATED_DATE);

        restScheduledTestDriveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScheduledTestDrive.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedScheduledTestDrive))
            )
            .andExpect(status().isOk());

        // Validate the ScheduledTestDrive in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertScheduledTestDriveUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedScheduledTestDrive, scheduledTestDrive),
            getPersistedScheduledTestDrive(scheduledTestDrive)
        );
    }

    @Test
    @Transactional
    void fullUpdateScheduledTestDriveWithPatch() throws Exception {
        // Initialize the database
        insertedScheduledTestDrive = scheduledTestDriveRepository.saveAndFlush(scheduledTestDrive);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the scheduledTestDrive using partial update
        ScheduledTestDrive partialUpdatedScheduledTestDrive = new ScheduledTestDrive();
        partialUpdatedScheduledTestDrive.setId(scheduledTestDrive.getId());

        partialUpdatedScheduledTestDrive.shopperId(UPDATED_SHOPPER_ID).date(UPDATED_DATE);

        restScheduledTestDriveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScheduledTestDrive.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedScheduledTestDrive))
            )
            .andExpect(status().isOk());

        // Validate the ScheduledTestDrive in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertScheduledTestDriveUpdatableFieldsEquals(
            partialUpdatedScheduledTestDrive,
            getPersistedScheduledTestDrive(partialUpdatedScheduledTestDrive)
        );
    }

    @Test
    @Transactional
    void patchNonExistingScheduledTestDrive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scheduledTestDrive.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScheduledTestDriveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, scheduledTestDrive.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(scheduledTestDrive))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduledTestDrive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchScheduledTestDrive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scheduledTestDrive.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduledTestDriveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(scheduledTestDrive))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduledTestDrive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamScheduledTestDrive() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scheduledTestDrive.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduledTestDriveMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(scheduledTestDrive)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ScheduledTestDrive in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteScheduledTestDrive() throws Exception {
        // Initialize the database
        insertedScheduledTestDrive = scheduledTestDriveRepository.saveAndFlush(scheduledTestDrive);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the scheduledTestDrive
        restScheduledTestDriveMockMvc
            .perform(delete(ENTITY_API_URL_ID, scheduledTestDrive.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return scheduledTestDriveRepository.count();
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

    protected ScheduledTestDrive getPersistedScheduledTestDrive(ScheduledTestDrive scheduledTestDrive) {
        return scheduledTestDriveRepository.findById(scheduledTestDrive.getId()).orElseThrow();
    }

    protected void assertPersistedScheduledTestDriveToMatchAllProperties(ScheduledTestDrive expectedScheduledTestDrive) {
        assertScheduledTestDriveAllPropertiesEquals(expectedScheduledTestDrive, getPersistedScheduledTestDrive(expectedScheduledTestDrive));
    }

    protected void assertPersistedScheduledTestDriveToMatchUpdatableProperties(ScheduledTestDrive expectedScheduledTestDrive) {
        assertScheduledTestDriveAllUpdatablePropertiesEquals(
            expectedScheduledTestDrive,
            getPersistedScheduledTestDrive(expectedScheduledTestDrive)
        );
    }
}
