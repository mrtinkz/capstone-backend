package com.capstone.ecommplatform.service.impl;

import com.capstone.ecommplatform.domain.PaymentDetail;
import com.capstone.ecommplatform.repository.PaymentDetailRepository;
import com.capstone.ecommplatform.service.PaymentDetailService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.capstone.ecommplatform.domain.PaymentDetail}.
 */
@Service
@Transactional
public class PaymentDetailServiceImpl implements PaymentDetailService {

    private final Logger log = LoggerFactory.getLogger(PaymentDetailServiceImpl.class);

    private final PaymentDetailRepository paymentDetailRepository;

    public PaymentDetailServiceImpl(PaymentDetailRepository paymentDetailRepository) {
        this.paymentDetailRepository = paymentDetailRepository;
    }

    @Override
    public PaymentDetail save(PaymentDetail paymentDetail) {
        log.debug("Request to save PaymentDetail : {}", paymentDetail);
        return paymentDetailRepository.save(paymentDetail);
    }

    @Override
    public PaymentDetail update(PaymentDetail paymentDetail) {
        log.debug("Request to update PaymentDetail : {}", paymentDetail);
        return paymentDetailRepository.save(paymentDetail);
    }

    @Override
    public Optional<PaymentDetail> partialUpdate(PaymentDetail paymentDetail) {
        log.debug("Request to partially update PaymentDetail : {}", paymentDetail);

        return paymentDetailRepository
            .findById(paymentDetail.getId())
            .map(existingPaymentDetail -> {
                if (paymentDetail.getOrderId() != null) {
                    existingPaymentDetail.setOrderId(paymentDetail.getOrderId());
                }
                if (paymentDetail.getPaymentType() != null) {
                    existingPaymentDetail.setPaymentType(paymentDetail.getPaymentType());
                }
                if (paymentDetail.getPaymentAmount() != null) {
                    existingPaymentDetail.setPaymentAmount(paymentDetail.getPaymentAmount());
                }
                if (paymentDetail.getPaymentDate() != null) {
                    existingPaymentDetail.setPaymentDate(paymentDetail.getPaymentDate());
                }
                if (paymentDetail.getCardType() != null) {
                    existingPaymentDetail.setCardType(paymentDetail.getCardType());
                }
                if (paymentDetail.getCardNumber() != null) {
                    existingPaymentDetail.setCardNumber(paymentDetail.getCardNumber());
                }
                if (paymentDetail.getCardHolderName() != null) {
                    existingPaymentDetail.setCardHolderName(paymentDetail.getCardHolderName());
                }
                if (paymentDetail.getCardExpirationDate() != null) {
                    existingPaymentDetail.setCardExpirationDate(paymentDetail.getCardExpirationDate());
                }

                return existingPaymentDetail;
            })
            .map(paymentDetailRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentDetail> findAll() {
        log.debug("Request to get all PaymentDetails");
        return paymentDetailRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentDetail> findOne(Long id) {
        log.debug("Request to get PaymentDetail : {}", id);
        return paymentDetailRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PaymentDetail : {}", id);
        paymentDetailRepository.deleteById(id);
    }
}
