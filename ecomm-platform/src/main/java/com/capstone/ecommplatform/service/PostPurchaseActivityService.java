package com.capstone.ecommplatform.service;

import com.capstone.ecommplatform.domain.PostPurchaseActivity;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.capstone.ecommplatform.domain.PostPurchaseActivity}.
 */
public interface PostPurchaseActivityService {
    /**
     * Save a postPurchaseActivity.
     *
     * @param postPurchaseActivity the entity to save.
     * @return the persisted entity.
     */
    PostPurchaseActivity save(PostPurchaseActivity postPurchaseActivity);

    /**
     * Updates a postPurchaseActivity.
     *
     * @param postPurchaseActivity the entity to update.
     * @return the persisted entity.
     */
    PostPurchaseActivity update(PostPurchaseActivity postPurchaseActivity);

    /**
     * Partially updates a postPurchaseActivity.
     *
     * @param postPurchaseActivity the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PostPurchaseActivity> partialUpdate(PostPurchaseActivity postPurchaseActivity);

    /**
     * Get all the postPurchaseActivities.
     *
     * @return the list of entities.
     */
    List<PostPurchaseActivity> findAll();

    /**
     * Get the "id" postPurchaseActivity.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PostPurchaseActivity> findOne(Long id);

    /**
     * Delete the "id" postPurchaseActivity.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
