package com.capstone.ecommplatform.web.rest;

import static com.capstone.ecommplatform.domain.ScheduledPickupAsserts.*;
import static com.capstone.ecommplatform.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.capstone.ecommplatform.IntegrationTest;
import com.capstone.ecommplatform.domain.ScheduledPickup;
import com.capstone.ecommplatform.repository.ScheduledPickupRepository;
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
 * Integration tests for the {@link ScheduledPickupResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ScheduledPickupResourceIT {

    private static final Long DEFAULT_SHOPPER_ID = 1L;
    private static final Long UPDATED_SHOPPER_ID = 2L;

    private static final Long DEFAULT_ORDER_ID = 1L;
    private static final Long UPDATED_ORDER_ID = 2L;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/scheduled-pickups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ScheduledPickupRepository scheduledPickupRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restScheduledPickupMockMvc;

    private ScheduledPickup scheduledPickup;

    private ScheduledPickup insertedScheduledPickup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ScheduledPickup createEntity(EntityManager em) {
        ScheduledPickup scheduledPickup = new ScheduledPickup().shopperId(DEFAULT_SHOPPER_ID).orderId(DEFAULT_ORDER_ID).date(DEFAULT_DATE);
        return scheduledPickup;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ScheduledPickup createUpdatedEntity(EntityManager em) {
        ScheduledPickup scheduledPickup = new ScheduledPickup().shopperId(UPDATED_SHOPPER_ID).orderId(UPDATED_ORDER_ID).date(UPDATED_DATE);
        return scheduledPickup;
    }

    @BeforeEach
    public void initTest() {
        scheduledPickup = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedScheduledPickup != null) {
            scheduledPickupRepository.delete(insertedScheduledPickup);
            insertedScheduledPickup = null;
        }
    }

    @Test
    @Transactional
    void createScheduledPickup() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ScheduledPickup
        var returnedScheduledPickup = om.readValue(
            restScheduledPickupMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scheduledPickup)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ScheduledPickup.class
        );

        // Validate the ScheduledPickup in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertScheduledPickupUpdatableFieldsEquals(returnedScheduledPickup, getPersistedScheduledPickup(returnedScheduledPickup));

        insertedScheduledPickup = returnedScheduledPickup;
    }

    @Test
    @Transactional
    void createScheduledPickupWithExistingId() throws Exception {
        // Create the ScheduledPickup with an existing ID
        scheduledPickup.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restScheduledPickupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scheduledPickup)))
            .andExpect(status().isBadRequest());

        // Validate the ScheduledPickup in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        scheduledPickup.setDate(null);

        // Create the ScheduledPickup, which fails.

        restScheduledPickupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scheduledPickup)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllScheduledPickups() throws Exception {
        // Initialize the database
        insertedScheduledPickup = scheduledPickupRepository.saveAndFlush(scheduledPickup);

        // Get all the scheduledPickupList
        restScheduledPickupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scheduledPickup.getId().intValue())))
            .andExpect(jsonPath("$.[*].shopperId").value(hasItem(DEFAULT_SHOPPER_ID.intValue())))
            .andExpect(jsonPath("$.[*].orderId").value(hasItem(DEFAULT_ORDER_ID.intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    void getScheduledPickup() throws Exception {
        // Initialize the database
        insertedScheduledPickup = scheduledPickupRepository.saveAndFlush(scheduledPickup);

        // Get the scheduledPickup
        restScheduledPickupMockMvc
            .perform(get(ENTITY_API_URL_ID, scheduledPickup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(scheduledPickup.getId().intValue()))
            .andExpect(jsonPath("$.shopperId").value(DEFAULT_SHOPPER_ID.intValue()))
            .andExpect(jsonPath("$.orderId").value(DEFAULT_ORDER_ID.intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingScheduledPickup() throws Exception {
        // Get the scheduledPickup
        restScheduledPickupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingScheduledPickup() throws Exception {
        // Initialize the database
        insertedScheduledPickup = scheduledPickupRepository.saveAndFlush(scheduledPickup);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the scheduledPickup
        ScheduledPickup updatedScheduledPickup = scheduledPickupRepository.findById(scheduledPickup.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedScheduledPickup are not directly saved in db
        em.detach(updatedScheduledPickup);
        updatedScheduledPickup.shopperId(UPDATED_SHOPPER_ID).orderId(UPDATED_ORDER_ID).date(UPDATED_DATE);

        restScheduledPickupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedScheduledPickup.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedScheduledPickup))
            )
            .andExpect(status().isOk());

        // Validate the ScheduledPickup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedScheduledPickupToMatchAllProperties(updatedScheduledPickup);
    }

    @Test
    @Transactional
    void putNonExistingScheduledPickup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scheduledPickup.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScheduledPickupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, scheduledPickup.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(scheduledPickup))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduledPickup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchScheduledPickup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scheduledPickup.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduledPickupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(scheduledPickup))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduledPickup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamScheduledPickup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scheduledPickup.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduledPickupMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scheduledPickup)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ScheduledPickup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateScheduledPickupWithPatch() throws Exception {
        // Initialize the database
        insertedScheduledPickup = scheduledPickupRepository.saveAndFlush(scheduledPickup);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the scheduledPickup using partial update
        ScheduledPickup partialUpdatedScheduledPickup = new ScheduledPickup();
        partialUpdatedScheduledPickup.setId(scheduledPickup.getId());

        partialUpdatedScheduledPickup.orderId(UPDATED_ORDER_ID).date(UPDATED_DATE);

        restScheduledPickupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScheduledPickup.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedScheduledPickup))
            )
            .andExpect(status().isOk());

        // Validate the ScheduledPickup in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertScheduledPickupUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedScheduledPickup, scheduledPickup),
            getPersistedScheduledPickup(scheduledPickup)
        );
    }

    @Test
    @Transactional
    void fullUpdateScheduledPickupWithPatch() throws Exception {
        // Initialize the database
        insertedScheduledPickup = scheduledPickupRepository.saveAndFlush(scheduledPickup);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the scheduledPickup using partial update
        ScheduledPickup partialUpdatedScheduledPickup = new ScheduledPickup();
        partialUpdatedScheduledPickup.setId(scheduledPickup.getId());

        partialUpdatedScheduledPickup.shopperId(UPDATED_SHOPPER_ID).orderId(UPDATED_ORDER_ID).date(UPDATED_DATE);

        restScheduledPickupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScheduledPickup.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedScheduledPickup))
            )
            .andExpect(status().isOk());

        // Validate the ScheduledPickup in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertScheduledPickupUpdatableFieldsEquals(
            partialUpdatedScheduledPickup,
            getPersistedScheduledPickup(partialUpdatedScheduledPickup)
        );
    }

    @Test
    @Transactional
    void patchNonExistingScheduledPickup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scheduledPickup.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScheduledPickupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, scheduledPickup.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(scheduledPickup))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduledPickup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchScheduledPickup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scheduledPickup.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduledPickupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(scheduledPickup))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduledPickup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamScheduledPickup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scheduledPickup.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduledPickupMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(scheduledPickup)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ScheduledPickup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteScheduledPickup() throws Exception {
        // Initialize the database
        insertedScheduledPickup = scheduledPickupRepository.saveAndFlush(scheduledPickup);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the scheduledPickup
        restScheduledPickupMockMvc
            .perform(delete(ENTITY_API_URL_ID, scheduledPickup.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return scheduledPickupRepository.count();
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

    protected ScheduledPickup getPersistedScheduledPickup(ScheduledPickup scheduledPickup) {
        return scheduledPickupRepository.findById(scheduledPickup.getId()).orElseThrow();
    }

    protected void assertPersistedScheduledPickupToMatchAllProperties(ScheduledPickup expectedScheduledPickup) {
        assertScheduledPickupAllPropertiesEquals(expectedScheduledPickup, getPersistedScheduledPickup(expectedScheduledPickup));
    }

    protected void assertPersistedScheduledPickupToMatchUpdatableProperties(ScheduledPickup expectedScheduledPickup) {
        assertScheduledPickupAllUpdatablePropertiesEquals(expectedScheduledPickup, getPersistedScheduledPickup(expectedScheduledPickup));
    }
}
