package com.capstone.ecommplatform.service.impl;

import com.capstone.ecommplatform.domain.Vehicle;
import com.capstone.ecommplatform.repository.VehicleRepository;
import com.capstone.ecommplatform.service.VehicleService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.capstone.ecommplatform.domain.Vehicle}.
 */
@Service
@Transactional
public class VehicleServiceImpl implements VehicleService {

    private final Logger log = LoggerFactory.getLogger(VehicleServiceImpl.class);

    private final VehicleRepository vehicleRepository;

    public VehicleServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        log.debug("Request to save Vehicle : {}", vehicle);
        return vehicleRepository.save(vehicle);
    }

    @Override
    public Vehicle update(Vehicle vehicle) {
        log.debug("Request to update Vehicle : {}", vehicle);
        return vehicleRepository.save(vehicle);
    }

    @Override
    public Optional<Vehicle> partialUpdate(Vehicle vehicle) {
        log.debug("Request to partially update Vehicle : {}", vehicle);

        return vehicleRepository
            .findById(vehicle.getId())
            .map(existingVehicle -> {
                if (vehicle.getVin() != null) {
                    existingVehicle.setVin(vehicle.getVin());
                }
                if (vehicle.getMake() != null) {
                    existingVehicle.setMake(vehicle.getMake());
                }
                if (vehicle.getModel() != null) {
                    existingVehicle.setModel(vehicle.getModel());
                }
                if (vehicle.getModelYear() != null) {
                    existingVehicle.setModelYear(vehicle.getModelYear());
                }
                if (vehicle.getDealerId() != null) {
                    existingVehicle.setDealerId(vehicle.getDealerId());
                }
                if (vehicle.getPricingSummaryId() != null) {
                    existingVehicle.setPricingSummaryId(vehicle.getPricingSummaryId());
                }
                if (vehicle.getVehicleOptionsId() != null) {
                    existingVehicle.setVehicleOptionsId(vehicle.getVehicleOptionsId());
                }

                return existingVehicle;
            })
            .map(vehicleRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehicle> findAll() {
        log.debug("Request to get all Vehicles");
        return vehicleRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Vehicle> findOne(Long id) {
        log.debug("Request to get Vehicle : {}", id);
        return vehicleRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Vehicle : {}", id);
        vehicleRepository.deleteById(id);
    }
}
