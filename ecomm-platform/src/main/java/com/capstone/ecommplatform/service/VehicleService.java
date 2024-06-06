package com.capstone.ecommplatform.service;

import com.capstone.ecommplatform.domain.Vehicle;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.capstone.ecommplatform.domain.Vehicle}.
 */
public interface VehicleService {
    /**
     * Save a vehicle.
     *
     * @param vehicle the entity to save.
     * @return the persisted entity.
     */
    Vehicle save(Vehicle vehicle);

    /**
     * Updates a vehicle.
     *
     * @param vehicle the entity to update.
     * @return the persisted entity.
     */
    Vehicle update(Vehicle vehicle);

    /**
     * Partially updates a vehicle.
     *
     * @param vehicle the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Vehicle> partialUpdate(Vehicle vehicle);

    /**
     * Get all the vehicles.
     *
     * @return the list of entities.
     */
    List<Vehicle> findAll();

    /**
     * Get the "id" vehicle.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Vehicle> findOne(Long id);

    /**
     * Delete the "id" vehicle.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
