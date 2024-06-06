package com.capstone.ecommplatform.web.rest;

import static com.capstone.ecommplatform.domain.PostPurchaseActivityAsserts.*;
import static com.capstone.ecommplatform.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.capstone.ecommplatform.IntegrationTest;
import com.capstone.ecommplatform.domain.PostPurchaseActivity;
import com.capstone.ecommplatform.domain.enumeration.PostPurchaseActivityStatus;
import com.capstone.ecommplatform.domain.enumeration.PostPurchaseActivityType;
import com.capstone.ecommplatform.repository.PostPurchaseActivityRepository;
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
 * Integration tests for the {@link PostPurchaseActivityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PostPurchaseActivityResourceIT {

    private static final Long DEFAULT_ORDER_ID = 1L;
    private static final Long UPDATED_ORDER_ID = 2L;

    private static final PostPurchaseActivityType DEFAULT_ACTIVITY_TYPE = PostPurchaseActivityType.PAPERWORK_COMPLETION;
    private static final PostPurchaseActivityType UPDATED_ACTIVITY_TYPE = PostPurchaseActivityType.UPLOAD_DOCUMENTS;

    private static final PostPurchaseActivityStatus DEFAULT_STATUS = PostPurchaseActivityStatus.PENDING;
    private static final PostPurchaseActivityStatus UPDATED_STATUS = PostPurchaseActivityStatus.IN_PROGRESS;

    private static final String ENTITY_API_URL = "/api/post-purchase-activities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PostPurchaseActivityRepository postPurchaseActivityRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPostPurchaseActivityMockMvc;

    private PostPurchaseActivity postPurchaseActivity;

    private PostPurchaseActivity insertedPostPurchaseActivity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostPurchaseActivity createEntity(EntityManager em) {
        PostPurchaseActivity postPurchaseActivity = new PostPurchaseActivity()
            .orderId(DEFAULT_ORDER_ID)
            .activityType(DEFAULT_ACTIVITY_TYPE)
            .status(DEFAULT_STATUS);
        return postPurchaseActivity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostPurchaseActivity createUpdatedEntity(EntityManager em) {
        PostPurchaseActivity postPurchaseActivity = new PostPurchaseActivity()
            .orderId(UPDATED_ORDER_ID)
            .activityType(UPDATED_ACTIVITY_TYPE)
            .status(UPDATED_STATUS);
        return postPurchaseActivity;
    }

    @BeforeEach
    public void initTest() {
        postPurchaseActivity = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedPostPurchaseActivity != null) {
            postPurchaseActivityRepository.delete(insertedPostPurchaseActivity);
            insertedPostPurchaseActivity = null;
        }
    }

    @Test
    @Transactional
    void createPostPurchaseActivity() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PostPurchaseActivity
        var returnedPostPurchaseActivity = om.readValue(
            restPostPurchaseActivityMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postPurchaseActivity)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PostPurchaseActivity.class
        );

        // Validate the PostPurchaseActivity in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPostPurchaseActivityUpdatableFieldsEquals(
            returnedPostPurchaseActivity,
            getPersistedPostPurchaseActivity(returnedPostPurchaseActivity)
        );

        insertedPostPurchaseActivity = returnedPostPurchaseActivity;
    }

    @Test
    @Transactional
    void createPostPurchaseActivityWithExistingId() throws Exception {
        // Create the PostPurchaseActivity with an existing ID
        postPurchaseActivity.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPostPurchaseActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postPurchaseActivity)))
            .andExpect(status().isBadRequest());

        // Validate the PostPurchaseActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkActivityTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        postPurchaseActivity.setActivityType(null);

        // Create the PostPurchaseActivity, which fails.

        restPostPurchaseActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postPurchaseActivity)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        postPurchaseActivity.setStatus(null);

        // Create the PostPurchaseActivity, which fails.

        restPostPurchaseActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postPurchaseActivity)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPostPurchaseActivities() throws Exception {
        // Initialize the database
        insertedPostPurchaseActivity = postPurchaseActivityRepository.saveAndFlush(postPurchaseActivity);

        // Get all the postPurchaseActivityList
        restPostPurchaseActivityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(postPurchaseActivity.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderId").value(hasItem(DEFAULT_ORDER_ID.intValue())))
            .andExpect(jsonPath("$.[*].activityType").value(hasItem(DEFAULT_ACTIVITY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getPostPurchaseActivity() throws Exception {
        // Initialize the database
        insertedPostPurchaseActivity = postPurchaseActivityRepository.saveAndFlush(postPurchaseActivity);

        // Get the postPurchaseActivity
        restPostPurchaseActivityMockMvc
            .perform(get(ENTITY_API_URL_ID, postPurchaseActivity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(postPurchaseActivity.getId().intValue()))
            .andExpect(jsonPath("$.orderId").value(DEFAULT_ORDER_ID.intValue()))
            .andExpect(jsonPath("$.activityType").value(DEFAULT_ACTIVITY_TYPE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPostPurchaseActivity() throws Exception {
        // Get the postPurchaseActivity
        restPostPurchaseActivityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPostPurchaseActivity() throws Exception {
        // Initialize the database
        insertedPostPurchaseActivity = postPurchaseActivityRepository.saveAndFlush(postPurchaseActivity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the postPurchaseActivity
        PostPurchaseActivity updatedPostPurchaseActivity = postPurchaseActivityRepository
            .findById(postPurchaseActivity.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedPostPurchaseActivity are not directly saved in db
        em.detach(updatedPostPurchaseActivity);
        updatedPostPurchaseActivity.orderId(UPDATED_ORDER_ID).activityType(UPDATED_ACTIVITY_TYPE).status(UPDATED_STATUS);

        restPostPurchaseActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPostPurchaseActivity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPostPurchaseActivity))
            )
            .andExpect(status().isOk());

        // Validate the PostPurchaseActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPostPurchaseActivityToMatchAllProperties(updatedPostPurchaseActivity);
    }

    @Test
    @Transactional
    void putNonExistingPostPurchaseActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postPurchaseActivity.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostPurchaseActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, postPurchaseActivity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(postPurchaseActivity))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostPurchaseActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPostPurchaseActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postPurchaseActivity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostPurchaseActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(postPurchaseActivity))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostPurchaseActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPostPurchaseActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postPurchaseActivity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostPurchaseActivityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(postPurchaseActivity)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PostPurchaseActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePostPurchaseActivityWithPatch() throws Exception {
        // Initialize the database
        insertedPostPurchaseActivity = postPurchaseActivityRepository.saveAndFlush(postPurchaseActivity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the postPurchaseActivity using partial update
        PostPurchaseActivity partialUpdatedPostPurchaseActivity = new PostPurchaseActivity();
        partialUpdatedPostPurchaseActivity.setId(postPurchaseActivity.getId());

        partialUpdatedPostPurchaseActivity.orderId(UPDATED_ORDER_ID).activityType(UPDATED_ACTIVITY_TYPE).status(UPDATED_STATUS);

        restPostPurchaseActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPostPurchaseActivity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPostPurchaseActivity))
            )
            .andExpect(status().isOk());

        // Validate the PostPurchaseActivity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPostPurchaseActivityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPostPurchaseActivity, postPurchaseActivity),
            getPersistedPostPurchaseActivity(postPurchaseActivity)
        );
    }

    @Test
    @Transactional
    void fullUpdatePostPurchaseActivityWithPatch() throws Exception {
        // Initialize the database
        insertedPostPurchaseActivity = postPurchaseActivityRepository.saveAndFlush(postPurchaseActivity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the postPurchaseActivity using partial update
        PostPurchaseActivity partialUpdatedPostPurchaseActivity = new PostPurchaseActivity();
        partialUpdatedPostPurchaseActivity.setId(postPurchaseActivity.getId());

        partialUpdatedPostPurchaseActivity.orderId(UPDATED_ORDER_ID).activityType(UPDATED_ACTIVITY_TYPE).status(UPDATED_STATUS);

        restPostPurchaseActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPostPurchaseActivity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPostPurchaseActivity))
            )
            .andExpect(status().isOk());

        // Validate the PostPurchaseActivity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPostPurchaseActivityUpdatableFieldsEquals(
            partialUpdatedPostPurchaseActivity,
            getPersistedPostPurchaseActivity(partialUpdatedPostPurchaseActivity)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPostPurchaseActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postPurchaseActivity.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostPurchaseActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, postPurchaseActivity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(postPurchaseActivity))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostPurchaseActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPostPurchaseActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postPurchaseActivity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostPurchaseActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(postPurchaseActivity))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostPurchaseActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPostPurchaseActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        postPurchaseActivity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostPurchaseActivityMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(postPurchaseActivity)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PostPurchaseActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePostPurchaseActivity() throws Exception {
        // Initialize the database
        insertedPostPurchaseActivity = postPurchaseActivityRepository.saveAndFlush(postPurchaseActivity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the postPurchaseActivity
        restPostPurchaseActivityMockMvc
            .perform(delete(ENTITY_API_URL_ID, postPurchaseActivity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return postPurchaseActivityRepository.count();
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

    protected PostPurchaseActivity getPersistedPostPurchaseActivity(PostPurchaseActivity postPurchaseActivity) {
        return postPurchaseActivityRepository.findById(postPurchaseActivity.getId()).orElseThrow();
    }

    protected void assertPersistedPostPurchaseActivityToMatchAllProperties(PostPurchaseActivity expectedPostPurchaseActivity) {
        assertPostPurchaseActivityAllPropertiesEquals(
            expectedPostPurchaseActivity,
            getPersistedPostPurchaseActivity(expectedPostPurchaseActivity)
        );
    }

    protected void assertPersistedPostPurchaseActivityToMatchUpdatableProperties(PostPurchaseActivity expectedPostPurchaseActivity) {
        assertPostPurchaseActivityAllUpdatablePropertiesEquals(
            expectedPostPurchaseActivity,
            getPersistedPostPurchaseActivity(expectedPostPurchaseActivity)
        );
    }
}
