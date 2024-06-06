package com.capstone.ecommplatform.service;

import com.capstone.ecommplatform.domain.ScheduledPickup;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.capstone.ecommplatform.domain.ScheduledPickup}.
 */
public interface ScheduledPickupService {
    /**
     * Save a scheduledPickup.
     *
     * @param scheduledPickup the entity to save.
     * @return the persisted entity.
     */
    ScheduledPickup save(ScheduledPickup scheduledPickup);

    /**
     * Updates a scheduledPickup.
     *
     * @param scheduledPickup the entity to update.
     * @return the persisted entity.
     */
    ScheduledPickup update(ScheduledPickup scheduledPickup);

    /**
     * Partially updates a scheduledPickup.
     *
     * @param scheduledPickup the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ScheduledPickup> partialUpdate(ScheduledPickup scheduledPickup);

    /**
     * Get all the scheduledPickups.
     *
     * @return the list of entities.
     */
    List<ScheduledPickup> findAll();

    /**
     * Get the "id" scheduledPickup.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ScheduledPickup> findOne(Long id);

    /**
     * Delete the "id" scheduledPickup.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
