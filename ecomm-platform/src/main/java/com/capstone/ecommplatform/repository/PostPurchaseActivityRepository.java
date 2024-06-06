package com.capstone.ecommplatform.repository;

import com.capstone.ecommplatform.domain.PostPurchaseActivity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PostPurchaseActivity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PostPurchaseActivityRepository extends JpaRepository<PostPurchaseActivity, Long> {}
