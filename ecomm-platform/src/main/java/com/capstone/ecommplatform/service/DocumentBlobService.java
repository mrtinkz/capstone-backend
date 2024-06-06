package com.capstone.ecommplatform.service;

import com.capstone.ecommplatform.domain.DocumentBlob;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.capstone.ecommplatform.domain.DocumentBlob}.
 */
public interface DocumentBlobService {
    /**
     * Save a documentBlob.
     *
     * @param documentBlob the entity to save.
     * @return the persisted entity.
     */
    DocumentBlob save(DocumentBlob documentBlob);

    /**
     * Updates a documentBlob.
     *
     * @param documentBlob the entity to update.
     * @return the persisted entity.
     */
    DocumentBlob update(DocumentBlob documentBlob);

    /**
     * Partially updates a documentBlob.
     *
     * @param documentBlob the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DocumentBlob> partialUpdate(DocumentBlob documentBlob);

    /**
     * Get all the documentBlobs.
     *
     * @return the list of entities.
     */
    List<DocumentBlob> findAll();

    /**
     * Get the "id" documentBlob.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DocumentBlob> findOne(Long id);

    /**
     * Delete the "id" documentBlob.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
