package com.capstone.ecommplatform.repository;

import com.capstone.ecommplatform.domain.VehicleOptions;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the VehicleOptions entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VehicleOptionsRepository extends JpaRepository<VehicleOptions, Long> {}
