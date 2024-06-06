package com.capstone.ecommplatform.web.rest;

import static com.capstone.ecommplatform.domain.ShoppingCartAsserts.*;
import static com.capstone.ecommplatform.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.capstone.ecommplatform.IntegrationTest;
import com.capstone.ecommplatform.domain.ShoppingCart;
import com.capstone.ecommplatform.repository.ShoppingCartRepository;
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
 * Integration tests for the {@link ShoppingCartResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ShoppingCartResourceIT {

    private static final Long DEFAULT_SHOPPER_ID = 1L;
    private static final Long UPDATED_SHOPPER_ID = 2L;

    private static final Long DEFAULT_VEHICLE_ID = 1L;
    private static final Long UPDATED_VEHICLE_ID = 2L;

    private static final String ENTITY_API_URL = "/api/shopping-carts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShoppingCartMockMvc;

    private ShoppingCart shoppingCart;

    private ShoppingCart insertedShoppingCart;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShoppingCart createEntity(EntityManager em) {
        ShoppingCart shoppingCart = new ShoppingCart().shopperId(DEFAULT_SHOPPER_ID).vehicleId(DEFAULT_VEHICLE_ID);
        return shoppingCart;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShoppingCart createUpdatedEntity(EntityManager em) {
        ShoppingCart shoppingCart = new ShoppingCart().shopperId(UPDATED_SHOPPER_ID).vehicleId(UPDATED_VEHICLE_ID);
        return shoppingCart;
    }

    @BeforeEach
    public void initTest() {
        shoppingCart = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedShoppingCart != null) {
            shoppingCartRepository.delete(insertedShoppingCart);
            insertedShoppingCart = null;
        }
    }

    @Test
    @Transactional
    void createShoppingCart() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ShoppingCart
        var returnedShoppingCart = om.readValue(
            restShoppingCartMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shoppingCart)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ShoppingCart.class
        );

        // Validate the ShoppingCart in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertShoppingCartUpdatableFieldsEquals(returnedShoppingCart, getPersistedShoppingCart(returnedShoppingCart));

        insertedShoppingCart = returnedShoppingCart;
    }

    @Test
    @Transactional
    void createShoppingCartWithExistingId() throws Exception {
        // Create the ShoppingCart with an existing ID
        shoppingCart.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShoppingCartMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shoppingCart)))
            .andExpect(status().isBadRequest());

        // Validate the ShoppingCart in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllShoppingCarts() throws Exception {
        // Initialize the database
        insertedShoppingCart = shoppingCartRepository.saveAndFlush(shoppingCart);

        // Get all the shoppingCartList
        restShoppingCartMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shoppingCart.getId().intValue())))
            .andExpect(jsonPath("$.[*].shopperId").value(hasItem(DEFAULT_SHOPPER_ID.intValue())))
            .andExpect(jsonPath("$.[*].vehicleId").value(hasItem(DEFAULT_VEHICLE_ID.intValue())));
    }

    @Test
    @Transactional
    void getShoppingCart() throws Exception {
        // Initialize the database
        insertedShoppingCart = shoppingCartRepository.saveAndFlush(shoppingCart);

        // Get the shoppingCart
        restShoppingCartMockMvc
            .perform(get(ENTITY_API_URL_ID, shoppingCart.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shoppingCart.getId().intValue()))
            .andExpect(jsonPath("$.shopperId").value(DEFAULT_SHOPPER_ID.intValue()))
            .andExpect(jsonPath("$.vehicleId").value(DEFAULT_VEHICLE_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingShoppingCart() throws Exception {
        // Get the shoppingCart
        restShoppingCartMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingShoppingCart() throws Exception {
        // Initialize the database
        insertedShoppingCart = shoppingCartRepository.saveAndFlush(shoppingCart);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shoppingCart
        ShoppingCart updatedShoppingCart = shoppingCartRepository.findById(shoppingCart.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedShoppingCart are not directly saved in db
        em.detach(updatedShoppingCart);
        updatedShoppingCart.shopperId(UPDATED_SHOPPER_ID).vehicleId(UPDATED_VEHICLE_ID);

        restShoppingCartMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedShoppingCart.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedShoppingCart))
            )
            .andExpect(status().isOk());

        // Validate the ShoppingCart in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedShoppingCartToMatchAllProperties(updatedShoppingCart);
    }

    @Test
    @Transactional
    void putNonExistingShoppingCart() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shoppingCart.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShoppingCartMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shoppingCart.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shoppingCart))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShoppingCart in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShoppingCart() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shoppingCart.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShoppingCartMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shoppingCart))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShoppingCart in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShoppingCart() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shoppingCart.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShoppingCartMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shoppingCart)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShoppingCart in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShoppingCartWithPatch() throws Exception {
        // Initialize the database
        insertedShoppingCart = shoppingCartRepository.saveAndFlush(shoppingCart);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shoppingCart using partial update
        ShoppingCart partialUpdatedShoppingCart = new ShoppingCart();
        partialUpdatedShoppingCart.setId(shoppingCart.getId());

        partialUpdatedShoppingCart.vehicleId(UPDATED_VEHICLE_ID);

        restShoppingCartMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShoppingCart.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShoppingCart))
            )
            .andExpect(status().isOk());

        // Validate the ShoppingCart in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShoppingCartUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedShoppingCart, shoppingCart),
            getPersistedShoppingCart(shoppingCart)
        );
    }

    @Test
    @Transactional
    void fullUpdateShoppingCartWithPatch() throws Exception {
        // Initialize the database
        insertedShoppingCart = shoppingCartRepository.saveAndFlush(shoppingCart);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shoppingCart using partial update
        ShoppingCart partialUpdatedShoppingCart = new ShoppingCart();
        partialUpdatedShoppingCart.setId(shoppingCart.getId());

        partialUpdatedShoppingCart.shopperId(UPDATED_SHOPPER_ID).vehicleId(UPDATED_VEHICLE_ID);

        restShoppingCartMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShoppingCart.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShoppingCart))
            )
            .andExpect(status().isOk());

        // Validate the ShoppingCart in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShoppingCartUpdatableFieldsEquals(partialUpdatedShoppingCart, getPersistedShoppingCart(partialUpdatedShoppingCart));
    }

    @Test
    @Transactional
    void patchNonExistingShoppingCart() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shoppingCart.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShoppingCartMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shoppingCart.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shoppingCart))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShoppingCart in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShoppingCart() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shoppingCart.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShoppingCartMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shoppingCart))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShoppingCart in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShoppingCart() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shoppingCart.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShoppingCartMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(shoppingCart)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShoppingCart in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShoppingCart() throws Exception {
        // Initialize the database
        insertedShoppingCart = shoppingCartRepository.saveAndFlush(shoppingCart);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the shoppingCart
        restShoppingCartMockMvc
            .perform(delete(ENTITY_API_URL_ID, shoppingCart.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return shoppingCartRepository.count();
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

    protected ShoppingCart getPersistedShoppingCart(ShoppingCart shoppingCart) {
        return shoppingCartRepository.findById(shoppingCart.getId()).orElseThrow();
    }

    protected void assertPersistedShoppingCartToMatchAllProperties(ShoppingCart expectedShoppingCart) {
        assertShoppingCartAllPropertiesEquals(expectedShoppingCart, getPersistedShoppingCart(expectedShoppingCart));
    }

    protected void assertPersistedShoppingCartToMatchUpdatableProperties(ShoppingCart expectedShoppingCart) {
        assertShoppingCartAllUpdatablePropertiesEquals(expectedShoppingCart, getPersistedShoppingCart(expectedShoppingCart));
    }
}
