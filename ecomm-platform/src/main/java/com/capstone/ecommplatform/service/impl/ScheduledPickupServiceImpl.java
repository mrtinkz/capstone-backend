package com.capstone.ecommplatform.service.impl;

import com.capstone.ecommplatform.domain.ScheduledPickup;
import com.capstone.ecommplatform.repository.ScheduledPickupRepository;
import com.capstone.ecommplatform.service.ScheduledPickupService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.capstone.ecommplatform.domain.ScheduledPickup}.
 */
@Service
@Transactional
public class ScheduledPickupServiceImpl implements ScheduledPickupService {

    private final Logger log = LoggerFactory.getLogger(ScheduledPickupServiceImpl.class);

    private final ScheduledPickupRepository scheduledPickupRepository;

    public ScheduledPickupServiceImpl(ScheduledPickupRepository scheduledPickupRepository) {
        this.scheduledPickupRepository = scheduledPickupRepository;
    }

    @Override
    public ScheduledPickup save(ScheduledPickup scheduledPickup) {
        log.debug("Request to save ScheduledPickup : {}", scheduledPickup);
        return scheduledPickupRepository.save(scheduledPickup);
    }

    @Override
    public ScheduledPickup update(ScheduledPickup scheduledPickup) {
        log.debug("Request to update ScheduledPickup : {}", scheduledPickup);
        return scheduledPickupRepository.save(scheduledPickup);
    }

    @Override
    public Optional<ScheduledPickup> partialUpdate(ScheduledPickup scheduledPickup) {
        log.debug("Request to partially update ScheduledPickup : {}", scheduledPickup);

        return scheduledPickupRepository
            .findById(scheduledPickup.getId())
            .map(existingScheduledPickup -> {
                if (scheduledPickup.getShopperId() != null) {
                    existingScheduledPickup.setShopperId(scheduledPickup.getShopperId());
                }
                if (scheduledPickup.getOrderId() != null) {
                    existingScheduledPickup.setOrderId(scheduledPickup.getOrderId());
                }
                if (scheduledPickup.getDate() != null) {
                    existingScheduledPickup.setDate(scheduledPickup.getDate());
                }

                return existingScheduledPickup;
            })
            .map(scheduledPickupRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScheduledPickup> findAll() {
        log.debug("Request to get all ScheduledPickups");
        return scheduledPickupRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ScheduledPickup> findOne(Long id) {
        log.debug("Request to get ScheduledPickup : {}", id);
        return scheduledPickupRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ScheduledPickup : {}", id);
        scheduledPickupRepository.deleteById(id);
    }
}
