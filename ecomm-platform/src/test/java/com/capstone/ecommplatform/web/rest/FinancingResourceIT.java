package com.capstone.ecommplatform.web.rest;

import static com.capstone.ecommplatform.domain.FinancingAsserts.*;
import static com.capstone.ecommplatform.web.rest.TestUtil.createUpdateProxyForBean;
import static com.capstone.ecommplatform.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.capstone.ecommplatform.IntegrationTest;
import com.capstone.ecommplatform.domain.Financing;
import com.capstone.ecommplatform.repository.FinancingRepository;
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
 * Integration tests for the {@link FinancingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FinancingResourceIT {

    private static final String DEFAULT_PROVIDER = "AAAAAAAAAA";
    private static final String UPDATED_PROVIDER = "BBBBBBBBBB";

    private static final Float DEFAULT_INTEREST_RATE = 1F;
    private static final Float UPDATED_INTEREST_RATE = 2F;

    private static final Integer DEFAULT_LOAN_TERM = 1;
    private static final Integer UPDATED_LOAN_TERM = 2;

    private static final BigDecimal DEFAULT_DOWN_PAYMENT = new BigDecimal(1);
    private static final BigDecimal UPDATED_DOWN_PAYMENT = new BigDecimal(2);

    private static final Long DEFAULT_ORDER_ID = 1L;
    private static final Long UPDATED_ORDER_ID = 2L;

    private static final String ENTITY_API_URL = "/api/financings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FinancingRepository financingRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFinancingMockMvc;

    private Financing financing;

    private Financing insertedFinancing;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Financing createEntity(EntityManager em) {
        Financing financing = new Financing()
            .provider(DEFAULT_PROVIDER)
            .interestRate(DEFAULT_INTEREST_RATE)
            .loanTerm(DEFAULT_LOAN_TERM)
            .downPayment(DEFAULT_DOWN_PAYMENT)
            .orderId(DEFAULT_ORDER_ID);
        return financing;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Financing createUpdatedEntity(EntityManager em) {
        Financing financing = new Financing()
            .provider(UPDATED_PROVIDER)
            .interestRate(UPDATED_INTEREST_RATE)
            .loanTerm(UPDATED_LOAN_TERM)
            .downPayment(UPDATED_DOWN_PAYMENT)
            .orderId(UPDATED_ORDER_ID);
        return financing;
    }

    @BeforeEach
    public void initTest() {
        financing = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedFinancing != null) {
            financingRepository.delete(insertedFinancing);
            insertedFinancing = null;
        }
    }

    @Test
    @Transactional
    void createFinancing() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Financing
        var returnedFinancing = om.readValue(
            restFinancingMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(financing)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Financing.class
        );

        // Validate the Financing in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFinancingUpdatableFieldsEquals(returnedFinancing, getPersistedFinancing(returnedFinancing));

        insertedFinancing = returnedFinancing;
    }

    @Test
    @Transactional
    void createFinancingWithExistingId() throws Exception {
        // Create the Financing with an existing ID
        financing.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFinancingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(financing)))
            .andExpect(status().isBadRequest());

        // Validate the Financing in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkProviderIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        financing.setProvider(null);

        // Create the Financing, which fails.

        restFinancingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(financing)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInterestRateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        financing.setInterestRate(null);

        // Create the Financing, which fails.

        restFinancingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(financing)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLoanTermIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        financing.setLoanTerm(null);

        // Create the Financing, which fails.

        restFinancingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(financing)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFinancings() throws Exception {
        // Initialize the database
        insertedFinancing = financingRepository.saveAndFlush(financing);

        // Get all the financingList
        restFinancingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(financing.getId().intValue())))
            .andExpect(jsonPath("$.[*].provider").value(hasItem(DEFAULT_PROVIDER)))
            .andExpect(jsonPath("$.[*].interestRate").value(hasItem(DEFAULT_INTEREST_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].loanTerm").value(hasItem(DEFAULT_LOAN_TERM)))
            .andExpect(jsonPath("$.[*].downPayment").value(hasItem(sameNumber(DEFAULT_DOWN_PAYMENT))))
            .andExpect(jsonPath("$.[*].orderId").value(hasItem(DEFAULT_ORDER_ID.intValue())));
    }

    @Test
    @Transactional
    void getFinancing() throws Exception {
        // Initialize the database
        insertedFinancing = financingRepository.saveAndFlush(financing);

        // Get the financing
        restFinancingMockMvc
            .perform(get(ENTITY_API_URL_ID, financing.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(financing.getId().intValue()))
            .andExpect(jsonPath("$.provider").value(DEFAULT_PROVIDER))
            .andExpect(jsonPath("$.interestRate").value(DEFAULT_INTEREST_RATE.doubleValue()))
            .andExpect(jsonPath("$.loanTerm").value(DEFAULT_LOAN_TERM))
            .andExpect(jsonPath("$.downPayment").value(sameNumber(DEFAULT_DOWN_PAYMENT)))
            .andExpect(jsonPath("$.orderId").value(DEFAULT_ORDER_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingFinancing() throws Exception {
        // Get the financing
        restFinancingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFinancing() throws Exception {
        // Initialize the database
        insertedFinancing = financingRepository.saveAndFlush(financing);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the financing
        Financing updatedFinancing = financingRepository.findById(financing.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFinancing are not directly saved in db
        em.detach(updatedFinancing);
        updatedFinancing
            .provider(UPDATED_PROVIDER)
            .interestRate(UPDATED_INTEREST_RATE)
            .loanTerm(UPDATED_LOAN_TERM)
            .downPayment(UPDATED_DOWN_PAYMENT)
            .orderId(UPDATED_ORDER_ID);

        restFinancingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFinancing.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFinancing))
            )
            .andExpect(status().isOk());

        // Validate the Financing in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFinancingToMatchAllProperties(updatedFinancing);
    }

    @Test
    @Transactional
    void putNonExistingFinancing() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financing.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFinancingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, financing.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(financing))
            )
            .andExpect(status().isBadRequest());

        // Validate the Financing in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFinancing() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financing.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinancingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(financing))
            )
            .andExpect(status().isBadRequest());

        // Validate the Financing in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFinancing() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financing.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinancingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(financing)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Financing in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFinancingWithPatch() throws Exception {
        // Initialize the database
        insertedFinancing = financingRepository.saveAndFlush(financing);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the financing using partial update
        Financing partialUpdatedFinancing = new Financing();
        partialUpdatedFinancing.setId(financing.getId());

        partialUpdatedFinancing.provider(UPDATED_PROVIDER).downPayment(UPDATED_DOWN_PAYMENT).orderId(UPDATED_ORDER_ID);

        restFinancingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFinancing.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFinancing))
            )
            .andExpect(status().isOk());

        // Validate the Financing in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFinancingUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFinancing, financing),
            getPersistedFinancing(financing)
        );
    }

    @Test
    @Transactional
    void fullUpdateFinancingWithPatch() throws Exception {
        // Initialize the database
        insertedFinancing = financingRepository.saveAndFlush(financing);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the financing using partial update
        Financing partialUpdatedFinancing = new Financing();
        partialUpdatedFinancing.setId(financing.getId());

        partialUpdatedFinancing
            .provider(UPDATED_PROVIDER)
            .interestRate(UPDATED_INTEREST_RATE)
            .loanTerm(UPDATED_LOAN_TERM)
            .downPayment(UPDATED_DOWN_PAYMENT)
            .orderId(UPDATED_ORDER_ID);

        restFinancingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFinancing.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFinancing))
            )
            .andExpect(status().isOk());

        // Validate the Financing in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFinancingUpdatableFieldsEquals(partialUpdatedFinancing, getPersistedFinancing(partialUpdatedFinancing));
    }

    @Test
    @Transactional
    void patchNonExistingFinancing() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financing.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFinancingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, financing.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(financing))
            )
            .andExpect(status().isBadRequest());

        // Validate the Financing in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFinancing() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financing.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinancingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(financing))
            )
            .andExpect(status().isBadRequest());

        // Validate the Financing in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFinancing() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financing.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinancingMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(financing)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Financing in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFinancing() throws Exception {
        // Initialize the database
        insertedFinancing = financingRepository.saveAndFlush(financing);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the financing
        restFinancingMockMvc
            .perform(delete(ENTITY_API_URL_ID, financing.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return financingRepository.count();
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

    protected Financing getPersistedFinancing(Financing financing) {
        return financingRepository.findById(financing.getId()).orElseThrow();
    }

    protected void assertPersistedFinancingToMatchAllProperties(Financing expectedFinancing) {
        assertFinancingAllPropertiesEquals(expectedFinancing, getPersistedFinancing(expectedFinancing));
    }

    protected void assertPersistedFinancingToMatchUpdatableProperties(Financing expectedFinancing) {
        assertFinancingAllUpdatablePropertiesEquals(expectedFinancing, getPersistedFinancing(expectedFinancing));
    }
}
