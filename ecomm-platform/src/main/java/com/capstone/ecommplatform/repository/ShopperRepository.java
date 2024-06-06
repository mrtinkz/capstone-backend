package com.capstone.ecommplatform.repository;

import com.capstone.ecommplatform.domain.Shopper;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Shopper entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShopperRepository extends JpaRepository<Shopper, Long> {}
