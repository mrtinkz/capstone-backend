package com.capstone.ecommplatform.service.impl;

import com.capstone.ecommplatform.domain.ScheduledTestDrive;
import com.capstone.ecommplatform.repository.ScheduledTestDriveRepository;
import com.capstone.ecommplatform.service.ScheduledTestDriveService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.capstone.ecommplatform.domain.ScheduledTestDrive}.
 */
@Service
@Transactional
public class ScheduledTestDriveServiceImpl implements ScheduledTestDriveService {

    private final Logger log = LoggerFactory.getLogger(ScheduledTestDriveServiceImpl.class);

    private final ScheduledTestDriveRepository scheduledTestDriveRepository;

    public ScheduledTestDriveServiceImpl(ScheduledTestDriveRepository scheduledTestDriveRepository) {
        this.scheduledTestDriveRepository = scheduledTestDriveRepository;
    }

    @Override
    public ScheduledTestDrive save(ScheduledTestDrive scheduledTestDrive) {
        log.debug("Request to save ScheduledTestDrive : {}", scheduledTestDrive);
        return scheduledTestDriveRepository.save(scheduledTestDrive);
    }

    @Override
    public ScheduledTestDrive update(ScheduledTestDrive scheduledTestDrive) {
        log.debug("Request to update ScheduledTestDrive : {}", scheduledTestDrive);
        return scheduledTestDriveRepository.save(scheduledTestDrive);
    }

    @Override
    public Optional<ScheduledTestDrive> partialUpdate(ScheduledTestDrive scheduledTestDrive) {
        log.debug("Request to partially update ScheduledTestDrive : {}", scheduledTestDrive);

        return scheduledTestDriveRepository
            .findById(scheduledTestDrive.getId())
            .map(existingScheduledTestDrive -> {
                if (scheduledTestDrive.getShopperId() != null) {
                    existingScheduledTestDrive.setShopperId(scheduledTestDrive.getShopperId());
                }
                if (scheduledTestDrive.getDate() != null) {
                    existingScheduledTestDrive.setDate(scheduledTestDrive.getDate());
                }

                return existingScheduledTestDrive;
            })
            .map(scheduledTestDriveRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScheduledTestDrive> findAll() {
        log.debug("Request to get all ScheduledTestDrives");
        return scheduledTestDriveRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ScheduledTestDrive> findOne(Long id) {
        log.debug("Request to get ScheduledTestDrive : {}", id);
        return scheduledTestDriveRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ScheduledTestDrive : {}", id);
        scheduledTestDriveRepository.deleteById(id);
    }
}
