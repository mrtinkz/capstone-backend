package com.capstone.ecommplatform.service;

import com.capstone.ecommplatform.domain.PaymentDetail;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.capstone.ecommplatform.domain.PaymentDetail}.
 */
public interface PaymentDetailService {
    /**
     * Save a paymentDetail.
     *
     * @param paymentDetail the entity to save.
     * @return the persisted entity.
     */
    PaymentDetail save(PaymentDetail paymentDetail);

    /**
     * Updates a paymentDetail.
     *
     * @param paymentDetail the entity to update.
     * @return the persisted entity.
     */
    PaymentDetail update(PaymentDetail paymentDetail);

    /**
     * Partially updates a paymentDetail.
     *
     * @param paymentDetail the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PaymentDetail> partialUpdate(PaymentDetail paymentDetail);

    /**
     * Get all the paymentDetails.
     *
     * @return the list of entities.
     */
    List<PaymentDetail> findAll();

    /**
     * Get the "id" paymentDetail.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaymentDetail> findOne(Long id);

    /**
     * Delete the "id" paymentDetail.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
