package com.capstone.ecommplatform.service.impl;

import com.capstone.ecommplatform.domain.PostPurchaseActivity;
import com.capstone.ecommplatform.repository.PostPurchaseActivityRepository;
import com.capstone.ecommplatform.service.PostPurchaseActivityService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.capstone.ecommplatform.domain.PostPurchaseActivity}.
 */
@Service
@Transactional
public class PostPurchaseActivityServiceImpl implements PostPurchaseActivityService {

    private final Logger log = LoggerFactory.getLogger(PostPurchaseActivityServiceImpl.class);

    private final PostPurchaseActivityRepository postPurchaseActivityRepository;

    public PostPurchaseActivityServiceImpl(PostPurchaseActivityRepository postPurchaseActivityRepository) {
        this.postPurchaseActivityRepository = postPurchaseActivityRepository;
    }

    @Override
    public PostPurchaseActivity save(PostPurchaseActivity postPurchaseActivity) {
        log.debug("Request to save PostPurchaseActivity : {}", postPurchaseActivity);
        return postPurchaseActivityRepository.save(postPurchaseActivity);
    }

    @Override
    public PostPurchaseActivity update(PostPurchaseActivity postPurchaseActivity) {
        log.debug("Request to update PostPurchaseActivity : {}", postPurchaseActivity);
        return postPurchaseActivityRepository.save(postPurchaseActivity);
    }

    @Override
    public Optional<PostPurchaseActivity> partialUpdate(PostPurchaseActivity postPurchaseActivity) {
        log.debug("Request to partially update PostPurchaseActivity : {}", postPurchaseActivity);

        return postPurchaseActivityRepository
            .findById(postPurchaseActivity.getId())
            .map(existingPostPurchaseActivity -> {
                if (postPurchaseActivity.getOrderId() != null) {
                    existingPostPurchaseActivity.setOrderId(postPurchaseActivity.getOrderId());
                }
                if (postPurchaseActivity.getActivityType() != null) {
                    existingPostPurchaseActivity.setActivityType(postPurchaseActivity.getActivityType());
                }
                if (postPurchaseActivity.getStatus() != null) {
                    existingPostPurchaseActivity.setStatus(postPurchaseActivity.getStatus());
                }

                return existingPostPurchaseActivity;
            })
            .map(postPurchaseActivityRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostPurchaseActivity> findAll() {
        log.debug("Request to get all PostPurchaseActivities");
        return postPurchaseActivityRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PostPurchaseActivity> findOne(Long id) {
        log.debug("Request to get PostPurchaseActivity : {}", id);
        return postPurchaseActivityRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PostPurchaseActivity : {}", id);
        postPurchaseActivityRepository.deleteById(id);
    }
}
