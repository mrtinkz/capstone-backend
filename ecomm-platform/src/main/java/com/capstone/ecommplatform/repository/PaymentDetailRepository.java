package com.capstone.ecommplatform.repository;

import com.capstone.ecommplatform.domain.PaymentDetail;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PaymentDetail entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, Long> {}
