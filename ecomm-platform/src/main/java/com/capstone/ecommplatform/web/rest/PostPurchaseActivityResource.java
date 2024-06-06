package com.capstone.ecommplatform.web.rest;

import com.capstone.ecommplatform.domain.PostPurchaseActivity;
import com.capstone.ecommplatform.repository.PostPurchaseActivityRepository;
import com.capstone.ecommplatform.service.PostPurchaseActivityService;
import com.capstone.ecommplatform.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.capstone.ecommplatform.domain.PostPurchaseActivity}.
 */
@RestController
@RequestMapping("/api/post-purchase-activities")
public class PostPurchaseActivityResource {

    private final Logger log = LoggerFactory.getLogger(PostPurchaseActivityResource.class);

    private static final String ENTITY_NAME = "postPurchaseActivity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PostPurchaseActivityService postPurchaseActivityService;

    private final PostPurchaseActivityRepository postPurchaseActivityRepository;

    public PostPurchaseActivityResource(
        PostPurchaseActivityService postPurchaseActivityService,
        PostPurchaseActivityRepository postPurchaseActivityRepository
    ) {
        this.postPurchaseActivityService = postPurchaseActivityService;
        this.postPurchaseActivityRepository = postPurchaseActivityRepository;
    }

    /**
     * {@code POST  /post-purchase-activities} : Create a new postPurchaseActivity.
     *
     * @param postPurchaseActivity the postPurchaseActivity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new postPurchaseActivity, or with status {@code 400 (Bad Request)} if the postPurchaseActivity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PostPurchaseActivity> createPostPurchaseActivity(@Valid @RequestBody PostPurchaseActivity postPurchaseActivity)
        throws URISyntaxException {
        log.debug("REST request to save PostPurchaseActivity : {}", postPurchaseActivity);
        if (postPurchaseActivity.getId() != null) {
            throw new BadRequestAlertException("A new postPurchaseActivity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        postPurchaseActivity = postPurchaseActivityService.save(postPurchaseActivity);
        return ResponseEntity.created(new URI("/api/post-purchase-activities/" + postPurchaseActivity.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, postPurchaseActivity.getId().toString()))
            .body(postPurchaseActivity);
    }

    /**
     * {@code PUT  /post-purchase-activities/:id} : Updates an existing postPurchaseActivity.
     *
     * @param id the id of the postPurchaseActivity to save.
     * @param postPurchaseActivity the postPurchaseActivity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postPurchaseActivity,
     * or with status {@code 400 (Bad Request)} if the postPurchaseActivity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the postPurchaseActivity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PostPurchaseActivity> updatePostPurchaseActivity(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PostPurchaseActivity postPurchaseActivity
    ) throws URISyntaxException {
        log.debug("REST request to update PostPurchaseActivity : {}, {}", id, postPurchaseActivity);
        if (postPurchaseActivity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, postPurchaseActivity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!postPurchaseActivityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        postPurchaseActivity = postPurchaseActivityService.update(postPurchaseActivity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, postPurchaseActivity.getId().toString()))
            .body(postPurchaseActivity);
    }

    /**
     * {@code PATCH  /post-purchase-activities/:id} : Partial updates given fields of an existing postPurchaseActivity, field will ignore if it is null
     *
     * @param id the id of the postPurchaseActivity to save.
     * @param postPurchaseActivity the postPurchaseActivity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postPurchaseActivity,
     * or with status {@code 400 (Bad Request)} if the postPurchaseActivity is not valid,
     * or with status {@code 404 (Not Found)} if the postPurchaseActivity is not found,
     * or with status {@code 500 (Internal Server Error)} if the postPurchaseActivity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PostPurchaseActivity> partialUpdatePostPurchaseActivity(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PostPurchaseActivity postPurchaseActivity
    ) throws URISyntaxException {
        log.debug("REST request to partial update PostPurchaseActivity partially : {}, {}", id, postPurchaseActivity);
        if (postPurchaseActivity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, postPurchaseActivity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!postPurchaseActivityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PostPurchaseActivity> result = postPurchaseActivityService.partialUpdate(postPurchaseActivity);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, postPurchaseActivity.getId().toString())
        );
    }

    /**
     * {@code GET  /post-purchase-activities} : get all the postPurchaseActivities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of postPurchaseActivities in body.
     */
    @GetMapping("")
    public List<PostPurchaseActivity> getAllPostPurchaseActivities() {
        log.debug("REST request to get all PostPurchaseActivities");
        return postPurchaseActivityService.findAll();
    }

    /**
     * {@code GET  /post-purchase-activities/:id} : get the "id" postPurchaseActivity.
     *
     * @param id the id of the postPurchaseActivity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the postPurchaseActivity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostPurchaseActivity> getPostPurchaseActivity(@PathVariable("id") Long id) {
        log.debug("REST request to get PostPurchaseActivity : {}", id);
        Optional<PostPurchaseActivity> postPurchaseActivity = postPurchaseActivityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(postPurchaseActivity);
    }

    /**
     * {@code DELETE  /post-purchase-activities/:id} : delete the "id" postPurchaseActivity.
     *
     * @param id the id of the postPurchaseActivity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostPurchaseActivity(@PathVariable("id") Long id) {
        log.debug("REST request to delete PostPurchaseActivity : {}", id);
        postPurchaseActivityService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
