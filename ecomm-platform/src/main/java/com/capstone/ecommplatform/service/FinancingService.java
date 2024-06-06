package com.capstone.ecommplatform.service;

import com.capstone.ecommplatform.domain.Financing;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.capstone.ecommplatform.domain.Financing}.
 */
public interface FinancingService {
    /**
     * Save a financing.
     *
     * @param financing the entity to save.
     * @return the persisted entity.
     */
    Financing save(Financing financing);

    /**
     * Updates a financing.
     *
     * @param financing the entity to update.
     * @return the persisted entity.
     */
    Financing update(Financing financing);

    /**
     * Partially updates a financing.
     *
     * @param financing the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Financing> partialUpdate(Financing financing);

    /**
     * Get all the financings.
     *
     * @return the list of entities.
     */
    List<Financing> findAll();

    /**
     * Get the "id" financing.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Financing> findOne(Long id);

    /**
     * Delete the "id" financing.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
