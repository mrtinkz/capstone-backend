package com.capstone.ecommplatform.web.rest;

import static com.capstone.ecommplatform.domain.ShopperAsserts.*;
import static com.capstone.ecommplatform.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.capstone.ecommplatform.IntegrationTest;
import com.capstone.ecommplatform.domain.Shopper;
import com.capstone.ecommplatform.repository.ShopperRepository;
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
 * Integration tests for the {@link ShopperResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ShopperResourceIT {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/shoppers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ShopperRepository shopperRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShopperMockMvc;

    private Shopper shopper;

    private Shopper insertedShopper;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Shopper createEntity(EntityManager em) {
        Shopper shopper = new Shopper()
            .userId(DEFAULT_USER_ID)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL);
        return shopper;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Shopper createUpdatedEntity(EntityManager em) {
        Shopper shopper = new Shopper()
            .userId(UPDATED_USER_ID)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL);
        return shopper;
    }

    @BeforeEach
    public void initTest() {
        shopper = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedShopper != null) {
            shopperRepository.delete(insertedShopper);
            insertedShopper = null;
        }
    }

    @Test
    @Transactional
    void createShopper() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Shopper
        var returnedShopper = om.readValue(
            restShopperMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shopper)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Shopper.class
        );

        // Validate the Shopper in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertShopperUpdatableFieldsEquals(returnedShopper, getPersistedShopper(returnedShopper));

        insertedShopper = returnedShopper;
    }

    @Test
    @Transactional
    void createShopperWithExistingId() throws Exception {
        // Create the Shopper with an existing ID
        shopper.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShopperMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shopper)))
            .andExpect(status().isBadRequest());

        // Validate the Shopper in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllShoppers() throws Exception {
        // Initialize the database
        insertedShopper = shopperRepository.saveAndFlush(shopper);

        // Get all the shopperList
        restShopperMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shopper.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    void getShopper() throws Exception {
        // Initialize the database
        insertedShopper = shopperRepository.saveAndFlush(shopper);

        // Get the shopper
        restShopperMockMvc
            .perform(get(ENTITY_API_URL_ID, shopper.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shopper.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    void getNonExistingShopper() throws Exception {
        // Get the shopper
        restShopperMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingShopper() throws Exception {
        // Initialize the database
        insertedShopper = shopperRepository.saveAndFlush(shopper);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shopper
        Shopper updatedShopper = shopperRepository.findById(shopper.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedShopper are not directly saved in db
        em.detach(updatedShopper);
        updatedShopper.userId(UPDATED_USER_ID).firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME).email(UPDATED_EMAIL);

        restShopperMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedShopper.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedShopper))
            )
            .andExpect(status().isOk());

        // Validate the Shopper in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedShopperToMatchAllProperties(updatedShopper);
    }

    @Test
    @Transactional
    void putNonExistingShopper() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shopper.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShopperMockMvc
            .perform(put(ENTITY_API_URL_ID, shopper.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shopper)))
            .andExpect(status().isBadRequest());

        // Validate the Shopper in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShopper() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shopper.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShopperMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shopper))
            )
            .andExpect(status().isBadRequest());

        // Validate the Shopper in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShopper() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shopper.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShopperMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shopper)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Shopper in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShopperWithPatch() throws Exception {
        // Initialize the database
        insertedShopper = shopperRepository.saveAndFlush(shopper);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shopper using partial update
        Shopper partialUpdatedShopper = new Shopper();
        partialUpdatedShopper.setId(shopper.getId());

        partialUpdatedShopper.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME).email(UPDATED_EMAIL);

        restShopperMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShopper.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShopper))
            )
            .andExpect(status().isOk());

        // Validate the Shopper in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShopperUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedShopper, shopper), getPersistedShopper(shopper));
    }

    @Test
    @Transactional
    void fullUpdateShopperWithPatch() throws Exception {
        // Initialize the database
        insertedShopper = shopperRepository.saveAndFlush(shopper);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shopper using partial update
        Shopper partialUpdatedShopper = new Shopper();
        partialUpdatedShopper.setId(shopper.getId());

        partialUpdatedShopper.userId(UPDATED_USER_ID).firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME).email(UPDATED_EMAIL);

        restShopperMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShopper.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShopper))
            )
            .andExpect(status().isOk());

        // Validate the Shopper in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShopperUpdatableFieldsEquals(partialUpdatedShopper, getPersistedShopper(partialUpdatedShopper));
    }

    @Test
    @Transactional
    void patchNonExistingShopper() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shopper.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShopperMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shopper.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(shopper))
            )
            .andExpect(status().isBadRequest());

        // Validate the Shopper in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShopper() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shopper.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShopperMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shopper))
            )
            .andExpect(status().isBadRequest());

        // Validate the Shopper in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShopper() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shopper.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShopperMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(shopper)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Shopper in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShopper() throws Exception {
        // Initialize the database
        insertedShopper = shopperRepository.saveAndFlush(shopper);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the shopper
        restShopperMockMvc
            .perform(delete(ENTITY_API_URL_ID, shopper.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return shopperRepository.count();
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

    protected Shopper getPersistedShopper(Shopper shopper) {
        return shopperRepository.findById(shopper.getId()).orElseThrow();
    }

    protected void assertPersistedShopperToMatchAllProperties(Shopper expectedShopper) {
        assertShopperAllPropertiesEquals(expectedShopper, getPersistedShopper(expectedShopper));
    }

    protected void assertPersistedShopperToMatchUpdatableProperties(Shopper expectedShopper) {
        assertShopperAllUpdatablePropertiesEquals(expectedShopper, getPersistedShopper(expectedShopper));
    }
}
