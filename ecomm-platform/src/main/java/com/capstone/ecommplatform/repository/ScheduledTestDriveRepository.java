package com.capstone.ecommplatform.repository;

import com.capstone.ecommplatform.domain.ScheduledTestDrive;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ScheduledTestDrive entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScheduledTestDriveRepository extends JpaRepository<ScheduledTestDrive, Long> {}
