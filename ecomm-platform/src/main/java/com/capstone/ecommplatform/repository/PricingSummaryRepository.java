package com.capstone.ecommplatform.repository;

import com.capstone.ecommplatform.domain.PricingSummary;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PricingSummary entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PricingSummaryRepository extends JpaRepository<PricingSummary, Long> {}
