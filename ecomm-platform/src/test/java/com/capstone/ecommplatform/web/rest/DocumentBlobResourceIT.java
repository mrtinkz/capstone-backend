package com.capstone.ecommplatform.web.rest;

import static com.capstone.ecommplatform.domain.DocumentBlobAsserts.*;
import static com.capstone.ecommplatform.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.capstone.ecommplatform.IntegrationTest;
import com.capstone.ecommplatform.domain.DocumentBlob;
import com.capstone.ecommplatform.repository.DocumentBlobRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Base64;
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
 * Integration tests for the {@link DocumentBlobResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentBlobResourceIT {

    private static final Long DEFAULT_POST_PURCHASE_ACTIVITY_ID = 1L;
    private static final Long UPDATED_POST_PURCHASE_ACTIVITY_ID = 2L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MIME_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_MIME_TYPE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_DATA = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DATA = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_DATA_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DATA_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/document-blobs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DocumentBlobRepository documentBlobRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentBlobMockMvc;

    private DocumentBlob documentBlob;

    private DocumentBlob insertedDocumentBlob;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentBlob createEntity(EntityManager em) {
        DocumentBlob documentBlob = new DocumentBlob()
            .postPurchaseActivityId(DEFAULT_POST_PURCHASE_ACTIVITY_ID)
            .name(DEFAULT_NAME)
            .mimeType(DEFAULT_MIME_TYPE)
            .data(DEFAULT_DATA)
            .dataContentType(DEFAULT_DATA_CONTENT_TYPE);
        return documentBlob;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentBlob createUpdatedEntity(EntityManager em) {
        DocumentBlob documentBlob = new DocumentBlob()
            .postPurchaseActivityId(UPDATED_POST_PURCHASE_ACTIVITY_ID)
            .name(UPDATED_NAME)
            .mimeType(UPDATED_MIME_TYPE)
            .data(UPDATED_DATA)
            .dataContentType(UPDATED_DATA_CONTENT_TYPE);
        return documentBlob;
    }

    @BeforeEach
    public void initTest() {
        documentBlob = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedDocumentBlob != null) {
            documentBlobRepository.delete(insertedDocumentBlob);
            insertedDocumentBlob = null;
        }
    }

    @Test
    @Transactional
    void createDocumentBlob() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DocumentBlob
        var returnedDocumentBlob = om.readValue(
            restDocumentBlobMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentBlob)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DocumentBlob.class
        );

        // Validate the DocumentBlob in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDocumentBlobUpdatableFieldsEquals(returnedDocumentBlob, getPersistedDocumentBlob(returnedDocumentBlob));

        insertedDocumentBlob = returnedDocumentBlob;
    }

    @Test
    @Transactional
    void createDocumentBlobWithExistingId() throws Exception {
        // Create the DocumentBlob with an existing ID
        documentBlob.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentBlobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentBlob)))
            .andExpect(status().isBadRequest());

        // Validate the DocumentBlob in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDocumentBlobs() throws Exception {
        // Initialize the database
        insertedDocumentBlob = documentBlobRepository.saveAndFlush(documentBlob);

        // Get all the documentBlobList
        restDocumentBlobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentBlob.getId().intValue())))
            .andExpect(jsonPath("$.[*].postPurchaseActivityId").value(hasItem(DEFAULT_POST_PURCHASE_ACTIVITY_ID.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].mimeType").value(hasItem(DEFAULT_MIME_TYPE)))
            .andExpect(jsonPath("$.[*].dataContentType").value(hasItem(DEFAULT_DATA_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_DATA))));
    }

    @Test
    @Transactional
    void getDocumentBlob() throws Exception {
        // Initialize the database
        insertedDocumentBlob = documentBlobRepository.saveAndFlush(documentBlob);

        // Get the documentBlob
        restDocumentBlobMockMvc
            .perform(get(ENTITY_API_URL_ID, documentBlob.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentBlob.getId().intValue()))
            .andExpect(jsonPath("$.postPurchaseActivityId").value(DEFAULT_POST_PURCHASE_ACTIVITY_ID.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.mimeType").value(DEFAULT_MIME_TYPE))
            .andExpect(jsonPath("$.dataContentType").value(DEFAULT_DATA_CONTENT_TYPE))
            .andExpect(jsonPath("$.data").value(Base64.getEncoder().encodeToString(DEFAULT_DATA)));
    }

    @Test
    @Transactional
    void getNonExistingDocumentBlob() throws Exception {
        // Get the documentBlob
        restDocumentBlobMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocumentBlob() throws Exception {
        // Initialize the database
        insertedDocumentBlob = documentBlobRepository.saveAndFlush(documentBlob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentBlob
        DocumentBlob updatedDocumentBlob = documentBlobRepository.findById(documentBlob.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDocumentBlob are not directly saved in db
        em.detach(updatedDocumentBlob);
        updatedDocumentBlob
            .postPurchaseActivityId(UPDATED_POST_PURCHASE_ACTIVITY_ID)
            .name(UPDATED_NAME)
            .mimeType(UPDATED_MIME_TYPE)
            .data(UPDATED_DATA)
            .dataContentType(UPDATED_DATA_CONTENT_TYPE);

        restDocumentBlobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDocumentBlob.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDocumentBlob))
            )
            .andExpect(status().isOk());

        // Validate the DocumentBlob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDocumentBlobToMatchAllProperties(updatedDocumentBlob);
    }

    @Test
    @Transactional
    void putNonExistingDocumentBlob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentBlob.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentBlobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentBlob.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentBlob))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentBlob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentBlob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentBlob.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentBlobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentBlob))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentBlob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentBlob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentBlob.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentBlobMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentBlob)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentBlob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocumentBlobWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentBlob = documentBlobRepository.saveAndFlush(documentBlob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentBlob using partial update
        DocumentBlob partialUpdatedDocumentBlob = new DocumentBlob();
        partialUpdatedDocumentBlob.setId(documentBlob.getId());

        partialUpdatedDocumentBlob.postPurchaseActivityId(UPDATED_POST_PURCHASE_ACTIVITY_ID).name(UPDATED_NAME);

        restDocumentBlobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentBlob.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentBlob))
            )
            .andExpect(status().isOk());

        // Validate the DocumentBlob in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentBlobUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDocumentBlob, documentBlob),
            getPersistedDocumentBlob(documentBlob)
        );
    }

    @Test
    @Transactional
    void fullUpdateDocumentBlobWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentBlob = documentBlobRepository.saveAndFlush(documentBlob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentBlob using partial update
        DocumentBlob partialUpdatedDocumentBlob = new DocumentBlob();
        partialUpdatedDocumentBlob.setId(documentBlob.getId());

        partialUpdatedDocumentBlob
            .postPurchaseActivityId(UPDATED_POST_PURCHASE_ACTIVITY_ID)
            .name(UPDATED_NAME)
            .mimeType(UPDATED_MIME_TYPE)
            .data(UPDATED_DATA)
            .dataContentType(UPDATED_DATA_CONTENT_TYPE);

        restDocumentBlobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentBlob.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentBlob))
            )
            .andExpect(status().isOk());

        // Validate the DocumentBlob in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentBlobUpdatableFieldsEquals(partialUpdatedDocumentBlob, getPersistedDocumentBlob(partialUpdatedDocumentBlob));
    }

    @Test
    @Transactional
    void patchNonExistingDocumentBlob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentBlob.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentBlobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentBlob.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentBlob))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentBlob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentBlob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentBlob.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentBlobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentBlob))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentBlob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentBlob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentBlob.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentBlobMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(documentBlob)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentBlob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocumentBlob() throws Exception {
        // Initialize the database
        insertedDocumentBlob = documentBlobRepository.saveAndFlush(documentBlob);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the documentBlob
        restDocumentBlobMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentBlob.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return documentBlobRepository.count();
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

    protected DocumentBlob getPersistedDocumentBlob(DocumentBlob documentBlob) {
        return documentBlobRepository.findById(documentBlob.getId()).orElseThrow();
    }

    protected void assertPersistedDocumentBlobToMatchAllProperties(DocumentBlob expectedDocumentBlob) {
        assertDocumentBlobAllPropertiesEquals(expectedDocumentBlob, getPersistedDocumentBlob(expectedDocumentBlob));
    }

    protected void assertPersistedDocumentBlobToMatchUpdatableProperties(DocumentBlob expectedDocumentBlob) {
        assertDocumentBlobAllUpdatablePropertiesEquals(expectedDocumentBlob, getPersistedDocumentBlob(expectedDocumentBlob));
    }
}
