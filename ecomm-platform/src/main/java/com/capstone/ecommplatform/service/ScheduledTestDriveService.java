package com.capstone.ecommplatform.service;

import com.capstone.ecommplatform.domain.ScheduledTestDrive;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.capstone.ecommplatform.domain.ScheduledTestDrive}.
 */
public interface ScheduledTestDriveService {
    /**
     * Save a scheduledTestDrive.
     *
     * @param scheduledTestDrive the entity to save.
     * @return the persisted entity.
     */
    ScheduledTestDrive save(ScheduledTestDrive scheduledTestDrive);

    /**
     * Updates a scheduledTestDrive.
     *
     * @param scheduledTestDrive the entity to update.
     * @return the persisted entity.
     */
    ScheduledTestDrive update(ScheduledTestDrive scheduledTestDrive);

    /**
     * Partially updates a scheduledTestDrive.
     *
     * @param scheduledTestDrive the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ScheduledTestDrive> partialUpdate(ScheduledTestDrive scheduledTestDrive);

    /**
     * Get all the scheduledTestDrives.
     *
     * @return the list of entities.
     */
    List<ScheduledTestDrive> findAll();

    /**
     * Get the "id" scheduledTestDrive.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ScheduledTestDrive> findOne(Long id);

    /**
     * Delete the "id" scheduledTestDrive.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
