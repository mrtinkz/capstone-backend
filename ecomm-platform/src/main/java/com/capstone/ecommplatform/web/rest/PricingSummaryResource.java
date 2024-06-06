package com.capstone.ecommplatform.web.rest;

import com.capstone.ecommplatform.domain.PricingSummary;
import com.capstone.ecommplatform.repository.PricingSummaryRepository;
import com.capstone.ecommplatform.service.PricingSummaryService;
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
 * REST controller for managing {@link com.capstone.ecommplatform.domain.PricingSummary}.
 */
@RestController
@RequestMapping("/api/pricing-summaries")
public class PricingSummaryResource {

    private final Logger log = LoggerFactory.getLogger(PricingSummaryResource.class);

    private static final String ENTITY_NAME = "pricingSummary";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PricingSummaryService pricingSummaryService;

    private final PricingSummaryRepository pricingSummaryRepository;

    public PricingSummaryResource(PricingSummaryService pricingSummaryService, PricingSummaryRepository pricingSummaryRepository) {
        this.pricingSummaryService = pricingSummaryService;
        this.pricingSummaryRepository = pricingSummaryRepository;
    }

    /**
     * {@code POST  /pricing-summaries} : Create a new pricingSummary.
     *
     * @param pricingSummary the pricingSummary to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pricingSummary, or with status {@code 400 (Bad Request)} if the pricingSummary has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PricingSummary> createPricingSummary(@Valid @RequestBody PricingSummary pricingSummary)
        throws URISyntaxException {
        log.debug("REST request to save PricingSummary : {}", pricingSummary);
        if (pricingSummary.getId() != null) {
            throw new BadRequestAlertException("A new pricingSummary cannot already have an ID", ENTITY_NAME, "idexists");
        }
        pricingSummary = pricingSummaryService.save(pricingSummary);
        return ResponseEntity.created(new URI("/api/pricing-summaries/" + pricingSummary.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, pricingSummary.getId().toString()))
            .body(pricingSummary);
    }

    /**
     * {@code PUT  /pricing-summaries/:id} : Updates an existing pricingSummary.
     *
     * @param id the id of the pricingSummary to save.
     * @param pricingSummary the pricingSummary to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pricingSummary,
     * or with status {@code 400 (Bad Request)} if the pricingSummary is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pricingSummary couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PricingSummary> updatePricingSummary(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PricingSummary pricingSummary
    ) throws URISyntaxException {
        log.debug("REST request to update PricingSummary : {}, {}", id, pricingSummary);
        if (pricingSummary.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pricingSummary.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pricingSummaryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        pricingSummary = pricingSummaryService.update(pricingSummary);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pricingSummary.getId().toString()))
            .body(pricingSummary);
    }

    /**
     * {@code PATCH  /pricing-summaries/:id} : Partial updates given fields of an existing pricingSummary, field will ignore if it is null
     *
     * @param id the id of the pricingSummary to save.
     * @param pricingSummary the pricingSummary to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pricingSummary,
     * or with status {@code 400 (Bad Request)} if the pricingSummary is not valid,
     * or with status {@code 404 (Not Found)} if the pricingSummary is not found,
     * or with status {@code 500 (Internal Server Error)} if the pricingSummary couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PricingSummary> partialUpdatePricingSummary(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PricingSummary pricingSummary
    ) throws URISyntaxException {
        log.debug("REST request to partial update PricingSummary partially : {}, {}", id, pricingSummary);
        if (pricingSummary.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pricingSummary.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pricingSummaryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PricingSummary> result = pricingSummaryService.partialUpdate(pricingSummary);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pricingSummary.getId().toString())
        );
    }

    /**
     * {@code GET  /pricing-summaries} : get all the pricingSummaries.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pricingSummaries in body.
     */
    @GetMapping("")
    public List<PricingSummary> getAllPricingSummaries() {
        log.debug("REST request to get all PricingSummaries");
        return pricingSummaryService.findAll();
    }

    /**
     * {@code GET  /pricing-summaries/:id} : get the "id" pricingSummary.
     *
     * @param id the id of the pricingSummary to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pricingSummary, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PricingSummary> getPricingSummary(@PathVariable("id") Long id) {
        log.debug("REST request to get PricingSummary : {}", id);
        Optional<PricingSummary> pricingSummary = pricingSummaryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pricingSummary);
    }

    /**
     * {@code DELETE  /pricing-summaries/:id} : delete the "id" pricingSummary.
     *
     * @param id the id of the pricingSummary to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePricingSummary(@PathVariable("id") Long id) {
        log.debug("REST request to delete PricingSummary : {}", id);
        pricingSummaryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
