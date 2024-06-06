package com.capstone.ecommplatform.web.rest;

import com.capstone.ecommplatform.domain.ScheduledPickup;
import com.capstone.ecommplatform.repository.ScheduledPickupRepository;
import com.capstone.ecommplatform.service.ScheduledPickupService;
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
 * REST controller for managing {@link com.capstone.ecommplatform.domain.ScheduledPickup}.
 */
@RestController
@RequestMapping("/api/scheduled-pickups")
public class ScheduledPickupResource {

    private final Logger log = LoggerFactory.getLogger(ScheduledPickupResource.class);

    private static final String ENTITY_NAME = "scheduledPickup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ScheduledPickupService scheduledPickupService;

    private final ScheduledPickupRepository scheduledPickupRepository;

    public ScheduledPickupResource(ScheduledPickupService scheduledPickupService, ScheduledPickupRepository scheduledPickupRepository) {
        this.scheduledPickupService = scheduledPickupService;
        this.scheduledPickupRepository = scheduledPickupRepository;
    }

    /**
     * {@code POST  /scheduled-pickups} : Create a new scheduledPickup.
     *
     * @param scheduledPickup the scheduledPickup to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new scheduledPickup, or with status {@code 400 (Bad Request)} if the scheduledPickup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ScheduledPickup> createScheduledPickup(@Valid @RequestBody ScheduledPickup scheduledPickup)
        throws URISyntaxException {
        log.debug("REST request to save ScheduledPickup : {}", scheduledPickup);
        if (scheduledPickup.getId() != null) {
            throw new BadRequestAlertException("A new scheduledPickup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        scheduledPickup = scheduledPickupService.save(scheduledPickup);
        return ResponseEntity.created(new URI("/api/scheduled-pickups/" + scheduledPickup.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, scheduledPickup.getId().toString()))
            .body(scheduledPickup);
    }

    /**
     * {@code PUT  /scheduled-pickups/:id} : Updates an existing scheduledPickup.
     *
     * @param id the id of the scheduledPickup to save.
     * @param scheduledPickup the scheduledPickup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scheduledPickup,
     * or with status {@code 400 (Bad Request)} if the scheduledPickup is not valid,
     * or with status {@code 500 (Internal Server Error)} if the scheduledPickup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ScheduledPickup> updateScheduledPickup(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ScheduledPickup scheduledPickup
    ) throws URISyntaxException {
        log.debug("REST request to update ScheduledPickup : {}, {}", id, scheduledPickup);
        if (scheduledPickup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scheduledPickup.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scheduledPickupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        scheduledPickup = scheduledPickupService.update(scheduledPickup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, scheduledPickup.getId().toString()))
            .body(scheduledPickup);
    }

    /**
     * {@code PATCH  /scheduled-pickups/:id} : Partial updates given fields of an existing scheduledPickup, field will ignore if it is null
     *
     * @param id the id of the scheduledPickup to save.
     * @param scheduledPickup the scheduledPickup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scheduledPickup,
     * or with status {@code 400 (Bad Request)} if the scheduledPickup is not valid,
     * or with status {@code 404 (Not Found)} if the scheduledPickup is not found,
     * or with status {@code 500 (Internal Server Error)} if the scheduledPickup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ScheduledPickup> partialUpdateScheduledPickup(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ScheduledPickup scheduledPickup
    ) throws URISyntaxException {
        log.debug("REST request to partial update ScheduledPickup partially : {}, {}", id, scheduledPickup);
        if (scheduledPickup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scheduledPickup.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scheduledPickupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ScheduledPickup> result = scheduledPickupService.partialUpdate(scheduledPickup);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, scheduledPickup.getId().toString())
        );
    }

    /**
     * {@code GET  /scheduled-pickups} : get all the scheduledPickups.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of scheduledPickups in body.
     */
    @GetMapping("")
    public List<ScheduledPickup> getAllScheduledPickups() {
        log.debug("REST request to get all ScheduledPickups");
        return scheduledPickupService.findAll();
    }

    /**
     * {@code GET  /scheduled-pickups/:id} : get the "id" scheduledPickup.
     *
     * @param id the id of the scheduledPickup to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the scheduledPickup, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ScheduledPickup> getScheduledPickup(@PathVariable("id") Long id) {
        log.debug("REST request to get ScheduledPickup : {}", id);
        Optional<ScheduledPickup> scheduledPickup = scheduledPickupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(scheduledPickup);
    }

    /**
     * {@code DELETE  /scheduled-pickups/:id} : delete the "id" scheduledPickup.
     *
     * @param id the id of the scheduledPickup to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScheduledPickup(@PathVariable("id") Long id) {
        log.debug("REST request to delete ScheduledPickup : {}", id);
        scheduledPickupService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
