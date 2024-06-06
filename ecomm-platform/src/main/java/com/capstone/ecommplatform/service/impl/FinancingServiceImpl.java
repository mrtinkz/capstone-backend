package com.capstone.ecommplatform.service.impl;

import com.capstone.ecommplatform.domain.Financing;
import com.capstone.ecommplatform.repository.FinancingRepository;
import com.capstone.ecommplatform.service.FinancingService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.capstone.ecommplatform.domain.Financing}.
 */
@Service
@Transactional
public class FinancingServiceImpl implements FinancingService {

    private final Logger log = LoggerFactory.getLogger(FinancingServiceImpl.class);

    private final FinancingRepository financingRepository;

    public FinancingServiceImpl(FinancingRepository financingRepository) {
        this.financingRepository = financingRepository;
    }

    @Override
    public Financing save(Financing financing) {
        log.debug("Request to save Financing : {}", financing);
        return financingRepository.save(financing);
    }

    @Override
    public Financing update(Financing financing) {
        log.debug("Request to update Financing : {}", financing);
        return financingRepository.save(financing);
    }

    @Override
    public Optional<Financing> partialUpdate(Financing financing) {
        log.debug("Request to partially update Financing : {}", financing);

        return financingRepository
            .findById(financing.getId())
            .map(existingFinancing -> {
                if (financing.getProvider() != null) {
                    existingFinancing.setProvider(financing.getProvider());
                }
                if (financing.getInterestRate() != null) {
                    existingFinancing.setInterestRate(financing.getInterestRate());
                }
                if (financing.getLoanTerm() != null) {
                    existingFinancing.setLoanTerm(financing.getLoanTerm());
                }
                if (financing.getDownPayment() != null) {
                    existingFinancing.setDownPayment(financing.getDownPayment());
                }
                if (financing.getOrderId() != null) {
                    existingFinancing.setOrderId(financing.getOrderId());
                }

                return existingFinancing;
            })
            .map(financingRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Financing> findAll() {
        log.debug("Request to get all Financings");
        return financingRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Financing> findOne(Long id) {
        log.debug("Request to get Financing : {}", id);
        return financingRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Financing : {}", id);
        financingRepository.deleteById(id);
    }
}
