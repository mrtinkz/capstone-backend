package com.capstone.ecommplatform.service.impl;

import com.capstone.ecommplatform.domain.PricingSummary;
import com.capstone.ecommplatform.repository.PricingSummaryRepository;
import com.capstone.ecommplatform.service.PricingSummaryService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.capstone.ecommplatform.domain.PricingSummary}.
 */
@Service
@Transactional
public class PricingSummaryServiceImpl implements PricingSummaryService {

    private final Logger log = LoggerFactory.getLogger(PricingSummaryServiceImpl.class);

    private final PricingSummaryRepository pricingSummaryRepository;

    public PricingSummaryServiceImpl(PricingSummaryRepository pricingSummaryRepository) {
        this.pricingSummaryRepository = pricingSummaryRepository;
    }

    @Override
    public PricingSummary save(PricingSummary pricingSummary) {
        log.debug("Request to save PricingSummary : {}", pricingSummary);
        return pricingSummaryRepository.save(pricingSummary);
    }

    @Override
    public PricingSummary update(PricingSummary pricingSummary) {
        log.debug("Request to update PricingSummary : {}", pricingSummary);
        return pricingSummaryRepository.save(pricingSummary);
    }

    @Override
    public Optional<PricingSummary> partialUpdate(PricingSummary pricingSummary) {
        log.debug("Request to partially update PricingSummary : {}", pricingSummary);

        return pricingSummaryRepository
            .findById(pricingSummary.getId())
            .map(existingPricingSummary -> {
                if (pricingSummary.getMsrp() != null) {
                    existingPricingSummary.setMsrp(pricingSummary.getMsrp());
                }
                if (pricingSummary.getTaxesAndFees() != null) {
                    existingPricingSummary.setTaxesAndFees(pricingSummary.getTaxesAndFees());
                }
                if (pricingSummary.getIncentives() != null) {
                    existingPricingSummary.setIncentives(pricingSummary.getIncentives());
                }
                if (pricingSummary.getTradeInEstimate() != null) {
                    existingPricingSummary.setTradeInEstimate(pricingSummary.getTradeInEstimate());
                }
                if (pricingSummary.getSubscriptionServices() != null) {
                    existingPricingSummary.setSubscriptionServices(pricingSummary.getSubscriptionServices());
                }
                if (pricingSummary.getProtectionPlan() != null) {
                    existingPricingSummary.setProtectionPlan(pricingSummary.getProtectionPlan());
                }

                return existingPricingSummary;
            })
            .map(pricingSummaryRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PricingSummary> findAll() {
        log.debug("Request to get all PricingSummaries");
        return pricingSummaryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PricingSummary> findOne(Long id) {
        log.debug("Request to get PricingSummary : {}", id);
        return pricingSummaryRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PricingSummary : {}", id);
        pricingSummaryRepository.deleteById(id);
    }
}
