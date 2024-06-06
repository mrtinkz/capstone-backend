package com.capstone.ecommplatform.repository;

import com.capstone.ecommplatform.domain.DocumentBlob;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DocumentBlob entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentBlobRepository extends JpaRepository<DocumentBlob, Long> {}
