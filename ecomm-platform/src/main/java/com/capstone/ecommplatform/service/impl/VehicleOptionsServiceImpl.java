package com.capstone.ecommplatform.service.impl;

import com.capstone.ecommplatform.domain.VehicleOptions;
import com.capstone.ecommplatform.repository.VehicleOptionsRepository;
import com.capstone.ecommplatform.service.VehicleOptionsService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.capstone.ecommplatform.domain.VehicleOptions}.
 */
@Service
@Transactional
public class VehicleOptionsServiceImpl implements VehicleOptionsService {

    private final Logger log = LoggerFactory.getLogger(VehicleOptionsServiceImpl.class);

    private final VehicleOptionsRepository vehicleOptionsRepository;

    public VehicleOptionsServiceImpl(VehicleOptionsRepository vehicleOptionsRepository) {
        this.vehicleOptionsRepository = vehicleOptionsRepository;
    }

    @Override
    public VehicleOptions save(VehicleOptions vehicleOptions) {
        log.debug("Request to save VehicleOptions : {}", vehicleOptions);
        return vehicleOptionsRepository.save(vehicleOptions);
    }

    @Override
    public VehicleOptions update(VehicleOptions vehicleOptions) {
        log.debug("Request to update VehicleOptions : {}", vehicleOptions);
        return vehicleOptionsRepository.save(vehicleOptions);
    }

    @Override
    public Optional<VehicleOptions> partialUpdate(VehicleOptions vehicleOptions) {
        log.debug("Request to partially update VehicleOptions : {}", vehicleOptions);

        return vehicleOptionsRepository
            .findById(vehicleOptions.getId())
            .map(existingVehicleOptions -> {
                if (vehicleOptions.getEstimatedMileage() != null) {
                    existingVehicleOptions.setEstimatedMileage(vehicleOptions.getEstimatedMileage());
                }
                if (vehicleOptions.getEngine() != null) {
                    existingVehicleOptions.setEngine(vehicleOptions.getEngine());
                }
                if (vehicleOptions.getDrivetrain() != null) {
                    existingVehicleOptions.setDrivetrain(vehicleOptions.getDrivetrain());
                }
                if (vehicleOptions.getTransmission() != null) {
                    existingVehicleOptions.setTransmission(vehicleOptions.getTransmission());
                }
                if (vehicleOptions.getTrim() != null) {
                    existingVehicleOptions.setTrim(vehicleOptions.getTrim());
                }
                if (vehicleOptions.getColor() != null) {
                    existingVehicleOptions.setColor(vehicleOptions.getColor());
                }

                return existingVehicleOptions;
            })
            .map(vehicleOptionsRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleOptions> findAll() {
        log.debug("Request to get all VehicleOptions");
        return vehicleOptionsRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VehicleOptions> findOne(Long id) {
        log.debug("Request to get VehicleOptions : {}", id);
        return vehicleOptionsRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete VehicleOptions : {}", id);
        vehicleOptionsRepository.deleteById(id);
    }
}
