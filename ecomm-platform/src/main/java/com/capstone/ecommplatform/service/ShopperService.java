package com.capstone.ecommplatform.service;

import com.capstone.ecommplatform.domain.Shopper;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.capstone.ecommplatform.domain.Shopper}.
 */
public interface ShopperService {
    /**
     * Save a shopper.
     *
     * @param shopper the entity to save.
     * @return the persisted entity.
     */
    Shopper save(Shopper shopper);

    /**
     * Updates a shopper.
     *
     * @param shopper the entity to update.
     * @return the persisted entity.
     */
    Shopper update(Shopper shopper);

    /**
     * Partially updates a shopper.
     *
     * @param shopper the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Shopper> partialUpdate(Shopper shopper);

    /**
     * Get all the shoppers.
     *
     * @return the list of entities.
     */
    List<Shopper> findAll();

    /**
     * Get the "id" shopper.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Shopper> findOne(Long id);

    /**
     * Delete the "id" shopper.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
