package com.capstone.ecommplatform.service;

import com.capstone.ecommplatform.domain.PricingSummary;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.capstone.ecommplatform.domain.PricingSummary}.
 */
public interface PricingSummaryService {
    /**
     * Save a pricingSummary.
     *
     * @param pricingSummary the entity to save.
     * @return the persisted entity.
     */
    PricingSummary save(PricingSummary pricingSummary);

    /**
     * Updates a pricingSummary.
     *
     * @param pricingSummary the entity to update.
     * @return the persisted entity.
     */
    PricingSummary update(PricingSummary pricingSummary);

    /**
     * Partially updates a pricingSummary.
     *
     * @param pricingSummary the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PricingSummary> partialUpdate(PricingSummary pricingSummary);

    /**
     * Get all the pricingSummaries.
     *
     * @return the list of entities.
     */
    List<PricingSummary> findAll();

    /**
     * Get the "id" pricingSummary.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PricingSummary> findOne(Long id);

    /**
     * Delete the "id" pricingSummary.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
