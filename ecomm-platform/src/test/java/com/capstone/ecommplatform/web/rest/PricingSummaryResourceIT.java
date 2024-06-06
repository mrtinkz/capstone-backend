package com.capstone.ecommplatform.web.rest;

import static com.capstone.ecommplatform.domain.PricingSummaryAsserts.*;
import static com.capstone.ecommplatform.web.rest.TestUtil.createUpdateProxyForBean;
import static com.capstone.ecommplatform.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.capstone.ecommplatform.IntegrationTest;
import com.capstone.ecommplatform.domain.PricingSummary;
import com.capstone.ecommplatform.repository.PricingSummaryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link PricingSummaryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PricingSummaryResourceIT {

    private static final BigDecimal DEFAULT_MSRP = new BigDecimal(1);
    private static final BigDecimal UPDATED_MSRP = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TAXES_AND_FEES = new BigDecimal(1);
    private static final BigDecimal UPDATED_TAXES_AND_FEES = new BigDecimal(2);

    private static final BigDecimal DEFAULT_INCENTIVES = new BigDecimal(1);
    private static final BigDecimal UPDATED_INCENTIVES = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TRADE_IN_ESTIMATE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TRADE_IN_ESTIMATE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_SUBSCRIPTION_SERVICES = new BigDecimal(1);
    private static final BigDecimal UPDATED_SUBSCRIPTION_SERVICES = new BigDecimal(2);

    private static final BigDecimal DEFAULT_PROTECTION_PLAN = new BigDecimal(1);
    private static final BigDecimal UPDATED_PROTECTION_PLAN = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/pricing-summaries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PricingSummaryRepository pricingSummaryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPricingSummaryMockMvc;

    private PricingSummary pricingSummary;

    private PricingSummary insertedPricingSummary;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PricingSummary createEntity(EntityManager em) {
        PricingSummary pricingSummary = new PricingSummary()
            .msrp(DEFAULT_MSRP)
            .taxesAndFees(DEFAULT_TAXES_AND_FEES)
            .incentives(DEFAULT_INCENTIVES)
            .tradeInEstimate(DEFAULT_TRADE_IN_ESTIMATE)
            .subscriptionServices(DEFAULT_SUBSCRIPTION_SERVICES)
            .protectionPlan(DEFAULT_PROTECTION_PLAN);
        return pricingSummary;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PricingSummary createUpdatedEntity(EntityManager em) {
        PricingSummary pricingSummary = new PricingSummary()
            .msrp(UPDATED_MSRP)
            .taxesAndFees(UPDATED_TAXES_AND_FEES)
            .incentives(UPDATED_INCENTIVES)
            .tradeInEstimate(UPDATED_TRADE_IN_ESTIMATE)
            .subscriptionServices(UPDATED_SUBSCRIPTION_SERVICES)
            .protectionPlan(UPDATED_PROTECTION_PLAN);
        return pricingSummary;
    }

    @BeforeEach
    public void initTest() {
        pricingSummary = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedPricingSummary != null) {
            pricingSummaryRepository.delete(insertedPricingSummary);
            insertedPricingSummary = null;
        }
    }

    @Test
    @Transactional
    void createPricingSummary() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PricingSummary
        var returnedPricingSummary = om.readValue(
            restPricingSummaryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pricingSummary)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PricingSummary.class
        );

        // Validate the PricingSummary in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPricingSummaryUpdatableFieldsEquals(returnedPricingSummary, getPersistedPricingSummary(returnedPricingSummary));

        insertedPricingSummary = returnedPricingSummary;
    }

    @Test
    @Transactional
    void createPricingSummaryWithExistingId() throws Exception {
        // Create the PricingSummary with an existing ID
        pricingSummary.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPricingSummaryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pricingSummary)))
            .andExpect(status().isBadRequest());

        // Validate the PricingSummary in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMsrpIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pricingSummary.setMsrp(null);

        // Create the PricingSummary, which fails.

        restPricingSummaryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pricingSummary)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTaxesAndFeesIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pricingSummary.setTaxesAndFees(null);

        // Create the PricingSummary, which fails.

        restPricingSummaryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pricingSummary)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPricingSummaries() throws Exception {
        // Initialize the database
        insertedPricingSummary = pricingSummaryRepository.saveAndFlush(pricingSummary);

        // Get all the pricingSummaryList
        restPricingSummaryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pricingSummary.getId().intValue())))
            .andExpect(jsonPath("$.[*].msrp").value(hasItem(sameNumber(DEFAULT_MSRP))))
            .andExpect(jsonPath("$.[*].taxesAndFees").value(hasItem(sameNumber(DEFAULT_TAXES_AND_FEES))))
            .andExpect(jsonPath("$.[*].incentives").value(hasItem(sameNumber(DEFAULT_INCENTIVES))))
            .andExpect(jsonPath("$.[*].tradeInEstimate").value(hasItem(sameNumber(DEFAULT_TRADE_IN_ESTIMATE))))
            .andExpect(jsonPath("$.[*].subscriptionServices").value(hasItem(sameNumber(DEFAULT_SUBSCRIPTION_SERVICES))))
            .andExpect(jsonPath("$.[*].protectionPlan").value(hasItem(sameNumber(DEFAULT_PROTECTION_PLAN))));
    }

    @Test
    @Transactional
    void getPricingSummary() throws Exception {
        // Initialize the database
        insertedPricingSummary = pricingSummaryRepository.saveAndFlush(pricingSummary);

        // Get the pricingSummary
        restPricingSummaryMockMvc
            .perform(get(ENTITY_API_URL_ID, pricingSummary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pricingSummary.getId().intValue()))
            .andExpect(jsonPath("$.msrp").value(sameNumber(DEFAULT_MSRP)))
            .andExpect(jsonPath("$.taxesAndFees").value(sameNumber(DEFAULT_TAXES_AND_FEES)))
            .andExpect(jsonPath("$.incentives").value(sameNumber(DEFAULT_INCENTIVES)))
            .andExpect(jsonPath("$.tradeInEstimate").value(sameNumber(DEFAULT_TRADE_IN_ESTIMATE)))
            .andExpect(jsonPath("$.subscriptionServices").value(sameNumber(DEFAULT_SUBSCRIPTION_SERVICES)))
            .andExpect(jsonPath("$.protectionPlan").value(sameNumber(DEFAULT_PROTECTION_PLAN)));
    }

    @Test
    @Transactional
    void getNonExistingPricingSummary() throws Exception {
        // Get the pricingSummary
        restPricingSummaryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPricingSummary() throws Exception {
        // Initialize the database
        insertedPricingSummary = pricingSummaryRepository.saveAndFlush(pricingSummary);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pricingSummary
        PricingSummary updatedPricingSummary = pricingSummaryRepository.findById(pricingSummary.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPricingSummary are not directly saved in db
        em.detach(updatedPricingSummary);
        updatedPricingSummary
            .msrp(UPDATED_MSRP)
            .taxesAndFees(UPDATED_TAXES_AND_FEES)
            .incentives(UPDATED_INCENTIVES)
            .tradeInEstimate(UPDATED_TRADE_IN_ESTIMATE)
            .subscriptionServices(UPDATED_SUBSCRIPTION_SERVICES)
            .protectionPlan(UPDATED_PROTECTION_PLAN);

        restPricingSummaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPricingSummary.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPricingSummary))
            )
            .andExpect(status().isOk());

        // Validate the PricingSummary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPricingSummaryToMatchAllProperties(updatedPricingSummary);
    }

    @Test
    @Transactional
    void putNonExistingPricingSummary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pricingSummary.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPricingSummaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pricingSummary.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pricingSummary))
            )
            .andExpect(status().isBadRequest());

        // Validate the PricingSummary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPricingSummary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pricingSummary.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPricingSummaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pricingSummary))
            )
            .andExpect(status().isBadRequest());

        // Validate the PricingSummary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPricingSummary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pricingSummary.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPricingSummaryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pricingSummary)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PricingSummary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePricingSummaryWithPatch() throws Exception {
        // Initialize the database
        insertedPricingSummary = pricingSummaryRepository.saveAndFlush(pricingSummary);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pricingSummary using partial update
        PricingSummary partialUpdatedPricingSummary = new PricingSummary();
        partialUpdatedPricingSummary.setId(pricingSummary.getId());

        partialUpdatedPricingSummary.msrp(UPDATED_MSRP).taxesAndFees(UPDATED_TAXES_AND_FEES).protectionPlan(UPDATED_PROTECTION_PLAN);

        restPricingSummaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPricingSummary.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPricingSummary))
            )
            .andExpect(status().isOk());

        // Validate the PricingSummary in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPricingSummaryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPricingSummary, pricingSummary),
            getPersistedPricingSummary(pricingSummary)
        );
    }

    @Test
    @Transactional
    void fullUpdatePricingSummaryWithPatch() throws Exception {
        // Initialize the database
        insertedPricingSummary = pricingSummaryRepository.saveAndFlush(pricingSummary);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pricingSummary using partial update
        PricingSummary partialUpdatedPricingSummary = new PricingSummary();
        partialUpdatedPricingSummary.setId(pricingSummary.getId());

        partialUpdatedPricingSummary
            .msrp(UPDATED_MSRP)
            .taxesAndFees(UPDATED_TAXES_AND_FEES)
            .incentives(UPDATED_INCENTIVES)
            .tradeInEstimate(UPDATED_TRADE_IN_ESTIMATE)
            .subscriptionServices(UPDATED_SUBSCRIPTION_SERVICES)
            .protectionPlan(UPDATED_PROTECTION_PLAN);

        restPricingSummaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPricingSummary.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPricingSummary))
            )
            .andExpect(status().isOk());

        // Validate the PricingSummary in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPricingSummaryUpdatableFieldsEquals(partialUpdatedPricingSummary, getPersistedPricingSummary(partialUpdatedPricingSummary));
    }

    @Test
    @Transactional
    void patchNonExistingPricingSummary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pricingSummary.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPricingSummaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pricingSummary.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pricingSummary))
            )
            .andExpect(status().isBadRequest());

        // Validate the PricingSummary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPricingSummary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pricingSummary.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPricingSummaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pricingSummary))
            )
            .andExpect(status().isBadRequest());

        // Validate the PricingSummary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPricingSummary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pricingSummary.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPricingSummaryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pricingSummary)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PricingSummary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePricingSummary() throws Exception {
        // Initialize the database
        insertedPricingSummary = pricingSummaryRepository.saveAndFlush(pricingSummary);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the pricingSummary
        restPricingSummaryMockMvc
            .perform(delete(ENTITY_API_URL_ID, pricingSummary.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return pricingSummaryRepository.count();
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

    protected PricingSummary getPersistedPricingSummary(PricingSummary pricingSummary) {
        return pricingSummaryRepository.findById(pricingSummary.getId()).orElseThrow();
    }

    protected void assertPersistedPricingSummaryToMatchAllProperties(PricingSummary expectedPricingSummary) {
        assertPricingSummaryAllPropertiesEquals(expectedPricingSummary, getPersistedPricingSummary(expectedPricingSummary));
    }

    protected void assertPersistedPricingSummaryToMatchUpdatableProperties(PricingSummary expectedPricingSummary) {
        assertPricingSummaryAllUpdatablePropertiesEquals(expectedPricingSummary, getPersistedPricingSummary(expectedPricingSummary));
    }
}
