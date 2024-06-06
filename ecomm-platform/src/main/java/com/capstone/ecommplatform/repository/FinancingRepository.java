package com.capstone.ecommplatform.repository;

import com.capstone.ecommplatform.domain.Financing;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Financing entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FinancingRepository extends JpaRepository<Financing, Long> {}
