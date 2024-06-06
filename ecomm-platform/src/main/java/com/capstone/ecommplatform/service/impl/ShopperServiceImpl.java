package com.capstone.ecommplatform.service.impl;

import com.capstone.ecommplatform.domain.Shopper;
import com.capstone.ecommplatform.repository.ShopperRepository;
import com.capstone.ecommplatform.service.ShopperService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.capstone.ecommplatform.domain.Shopper}.
 */
@Service
@Transactional
public class ShopperServiceImpl implements ShopperService {

    private final Logger log = LoggerFactory.getLogger(ShopperServiceImpl.class);

    private final ShopperRepository shopperRepository;

    public ShopperServiceImpl(ShopperRepository shopperRepository) {
        this.shopperRepository = shopperRepository;
    }

    @Override
    public Shopper save(Shopper shopper) {
        log.debug("Request to save Shopper : {}", shopper);
        return shopperRepository.save(shopper);
    }

    @Override
    public Shopper update(Shopper shopper) {
        log.debug("Request to update Shopper : {}", shopper);
        return shopperRepository.save(shopper);
    }

    @Override
    public Optional<Shopper> partialUpdate(Shopper shopper) {
        log.debug("Request to partially update Shopper : {}", shopper);

        return shopperRepository
            .findById(shopper.getId())
            .map(existingShopper -> {
                if (shopper.getUserId() != null) {
                    existingShopper.setUserId(shopper.getUserId());
                }
                if (shopper.getFirstName() != null) {
                    existingShopper.setFirstName(shopper.getFirstName());
                }
                if (shopper.getLastName() != null) {
                    existingShopper.setLastName(shopper.getLastName());
                }
                if (shopper.getEmail() != null) {
                    existingShopper.setEmail(shopper.getEmail());
                }

                return existingShopper;
            })
            .map(shopperRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Shopper> findAll() {
        log.debug("Request to get all Shoppers");
        return shopperRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Shopper> findOne(Long id) {
        log.debug("Request to get Shopper : {}", id);
        return shopperRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Shopper : {}", id);
        shopperRepository.deleteById(id);
    }
}
