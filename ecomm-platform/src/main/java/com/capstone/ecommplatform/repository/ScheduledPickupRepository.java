package com.capstone.ecommplatform.repository;

import com.capstone.ecommplatform.domain.ScheduledPickup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ScheduledPickup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScheduledPickupRepository extends JpaRepository<ScheduledPickup, Long> {}
