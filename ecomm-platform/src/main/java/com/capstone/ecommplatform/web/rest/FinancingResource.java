package com.capstone.ecommplatform.web.rest;

import com.capstone.ecommplatform.domain.Financing;
import com.capstone.ecommplatform.repository.FinancingRepository;
import com.capstone.ecommplatform.service.FinancingService;
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
 * REST controller for managing {@link com.capstone.ecommplatform.domain.Financing}.
 */
@RestController
@RequestMapping("/api/financings")
public class FinancingResource {

    private final Logger log = LoggerFactory.getLogger(FinancingResource.class);

    private static final String ENTITY_NAME = "financing";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FinancingService financingService;

    private final FinancingRepository financingRepository;

    public FinancingResource(FinancingService financingService, FinancingRepository financingRepository) {
        this.financingService = financingService;
        this.financingRepository = financingRepository;
    }

    /**
     * {@code POST  /financings} : Create a new financing.
     *
     * @param financing the financing to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new financing, or with status {@code 400 (Bad Request)} if the financing has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Financing> createFinancing(@Valid @RequestBody Financing financing) throws URISyntaxException {
        log.debug("REST request to save Financing : {}", financing);
        if (financing.getId() != null) {
            throw new BadRequestAlertException("A new financing cannot already have an ID", ENTITY_NAME, "idexists");
        }
        financing = financingService.save(financing);
        return ResponseEntity.created(new URI("/api/financings/" + financing.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, financing.getId().toString()))
            .body(financing);
    }

    /**
     * {@code PUT  /financings/:id} : Updates an existing financing.
     *
     * @param id the id of the financing to save.
     * @param financing the financing to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated financing,
     * or with status {@code 400 (Bad Request)} if the financing is not valid,
     * or with status {@code 500 (Internal Server Error)} if the financing couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Financing> updateFinancing(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Financing financing
    ) throws URISyntaxException {
        log.debug("REST request to update Financing : {}, {}", id, financing);
        if (financing.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, financing.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!financingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        financing = financingService.update(financing);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, financing.getId().toString()))
            .body(financing);
    }

    /**
     * {@code PATCH  /financings/:id} : Partial updates given fields of an existing financing, field will ignore if it is null
     *
     * @param id the id of the financing to save.
     * @param financing the financing to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated financing,
     * or with status {@code 400 (Bad Request)} if the financing is not valid,
     * or with status {@code 404 (Not Found)} if the financing is not found,
     * or with status {@code 500 (Internal Server Error)} if the financing couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Financing> partialUpdateFinancing(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Financing financing
    ) throws URISyntaxException {
        log.debug("REST request to partial update Financing partially : {}, {}", id, financing);
        if (financing.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, financing.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!financingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Financing> result = financingService.partialUpdate(financing);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, financing.getId().toString())
        );
    }

    /**
     * {@code GET  /financings} : get all the financings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of financings in body.
     */
    @GetMapping("")
    public List<Financing> getAllFinancings() {
        log.debug("REST request to get all Financings");
        return financingService.findAll();
    }

    /**
     * {@code GET  /financings/:id} : get the "id" financing.
     *
     * @param id the id of the financing to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the financing, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Financing> getFinancing(@PathVariable("id") Long id) {
        log.debug("REST request to get Financing : {}", id);
        Optional<Financing> financing = financingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(financing);
    }

    /**
     * {@code DELETE  /financings/:id} : delete the "id" financing.
     *
     * @param id the id of the financing to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFinancing(@PathVariable("id") Long id) {
        log.debug("REST request to delete Financing : {}", id);
        financingService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
