package com.capstone.ecommplatform.service;

import com.capstone.ecommplatform.domain.VehicleOptions;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.capstone.ecommplatform.domain.VehicleOptions}.
 */
public interface VehicleOptionsService {
    /**
     * Save a vehicleOptions.
     *
     * @param vehicleOptions the entity to save.
     * @return the persisted entity.
     */
    VehicleOptions save(VehicleOptions vehicleOptions);

    /**
     * Updates a vehicleOptions.
     *
     * @param vehicleOptions the entity to update.
     * @return the persisted entity.
     */
    VehicleOptions update(VehicleOptions vehicleOptions);

    /**
     * Partially updates a vehicleOptions.
     *
     * @param vehicleOptions the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VehicleOptions> partialUpdate(VehicleOptions vehicleOptions);

    /**
     * Get all the vehicleOptions.
     *
     * @return the list of entities.
     */
    List<VehicleOptions> findAll();

    /**
     * Get the "id" vehicleOptions.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VehicleOptions> findOne(Long id);

    /**
     * Delete the "id" vehicleOptions.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
