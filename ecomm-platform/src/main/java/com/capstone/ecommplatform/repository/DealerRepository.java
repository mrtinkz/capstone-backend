package com.capstone.ecommplatform.repository;

import com.capstone.ecommplatform.domain.Dealer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Dealer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DealerRepository extends JpaRepository<Dealer, Long> {}
