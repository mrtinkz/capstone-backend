package com.capstone.ecommplatform.web.rest;

import static com.capstone.ecommplatform.domain.DealerAsserts.*;
import static com.capstone.ecommplatform.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.capstone.ecommplatform.IntegrationTest;
import com.capstone.ecommplatform.domain.Dealer;
import com.capstone.ecommplatform.repository.DealerRepository;
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
 * Integration tests for the {@link DealerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DealerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_ZIP_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ZIP_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_WEBSITE = "AAAAAAAAAA";
    private static final String UPDATED_WEBSITE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/dealers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DealerRepository dealerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDealerMockMvc;

    private Dealer dealer;

    private Dealer insertedDealer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dealer createEntity(EntityManager em) {
        Dealer dealer = new Dealer()
            .name(DEFAULT_NAME)
            .address(DEFAULT_ADDRESS)
            .city(DEFAULT_CITY)
            .state(DEFAULT_STATE)
            .zipCode(DEFAULT_ZIP_CODE)
            .website(DEFAULT_WEBSITE);
        return dealer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dealer createUpdatedEntity(EntityManager em) {
        Dealer dealer = new Dealer()
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zipCode(UPDATED_ZIP_CODE)
            .website(UPDATED_WEBSITE);
        return dealer;
    }

    @BeforeEach
    public void initTest() {
        dealer = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedDealer != null) {
            dealerRepository.delete(insertedDealer);
            insertedDealer = null;
        }
    }

    @Test
    @Transactional
    void createDealer() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Dealer
        var returnedDealer = om.readValue(
            restDealerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dealer)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Dealer.class
        );

        // Validate the Dealer in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDealerUpdatableFieldsEquals(returnedDealer, getPersistedDealer(returnedDealer));

        insertedDealer = returnedDealer;
    }

    @Test
    @Transactional
    void createDealerWithExistingId() throws Exception {
        // Create the Dealer with an existing ID
        dealer.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDealerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dealer)))
            .andExpect(status().isBadRequest());

        // Validate the Dealer in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dealer.setName(null);

        // Create the Dealer, which fails.

        restDealerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dealer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDealers() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList
        restDealerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dealer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE)))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)));
    }

    @Test
    @Transactional
    void getDealer() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        // Get the dealer
        restDealerMockMvc
            .perform(get(ENTITY_API_URL_ID, dealer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dealer.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.zipCode").value(DEFAULT_ZIP_CODE))
            .andExpect(jsonPath("$.website").value(DEFAULT_WEBSITE));
    }

    @Test
    @Transactional
    void getNonExistingDealer() throws Exception {
        // Get the dealer
        restDealerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDealer() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dealer
        Dealer updatedDealer = dealerRepository.findById(dealer.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDealer are not directly saved in db
        em.detach(updatedDealer);
        updatedDealer
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zipCode(UPDATED_ZIP_CODE)
            .website(UPDATED_WEBSITE);

        restDealerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDealer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDealer))
            )
            .andExpect(status().isOk());

        // Validate the Dealer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDealerToMatchAllProperties(updatedDealer);
    }

    @Test
    @Transactional
    void putNonExistingDealer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dealer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDealerMockMvc
            .perform(put(ENTITY_API_URL_ID, dealer.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dealer)))
            .andExpect(status().isBadRequest());

        // Validate the Dealer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDealer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dealer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDealerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dealer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dealer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDealer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dealer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDealerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dealer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dealer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDealerWithPatch() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dealer using partial update
        Dealer partialUpdatedDealer = new Dealer();
        partialUpdatedDealer.setId(dealer.getId());

        partialUpdatedDealer.name(UPDATED_NAME).state(UPDATED_STATE).zipCode(UPDATED_ZIP_CODE);

        restDealerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDealer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDealer))
            )
            .andExpect(status().isOk());

        // Validate the Dealer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDealerUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedDealer, dealer), getPersistedDealer(dealer));
    }

    @Test
    @Transactional
    void fullUpdateDealerWithPatch() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dealer using partial update
        Dealer partialUpdatedDealer = new Dealer();
        partialUpdatedDealer.setId(dealer.getId());

        partialUpdatedDealer
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zipCode(UPDATED_ZIP_CODE)
            .website(UPDATED_WEBSITE);

        restDealerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDealer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDealer))
            )
            .andExpect(status().isOk());

        // Validate the Dealer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDealerUpdatableFieldsEquals(partialUpdatedDealer, getPersistedDealer(partialUpdatedDealer));
    }

    @Test
    @Transactional
    void patchNonExistingDealer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dealer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDealerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dealer.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(dealer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dealer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDealer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dealer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDealerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(dealer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dealer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDealer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dealer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDealerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(dealer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dealer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDealer() throws Exception {
        // Initialize the database
        insertedDealer = dealerRepository.saveAndFlush(dealer);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the dealer
        restDealerMockMvc
            .perform(delete(ENTITY_API_URL_ID, dealer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return dealerRepository.count();
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

    protected Dealer getPersistedDealer(Dealer dealer) {
        return dealerRepository.findById(dealer.getId()).orElseThrow();
    }

    protected void assertPersistedDealerToMatchAllProperties(Dealer expectedDealer) {
        assertDealerAllPropertiesEquals(expectedDealer, getPersistedDealer(expectedDealer));
    }

    protected void assertPersistedDealerToMatchUpdatableProperties(Dealer expectedDealer) {
        assertDealerAllUpdatablePropertiesEquals(expectedDealer, getPersistedDealer(expectedDealer));
    }
}
