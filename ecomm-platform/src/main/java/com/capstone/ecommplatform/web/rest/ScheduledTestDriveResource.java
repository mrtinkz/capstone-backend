package com.capstone.ecommplatform.web.rest;

import com.capstone.ecommplatform.domain.ScheduledTestDrive;
import com.capstone.ecommplatform.repository.ScheduledTestDriveRepository;
import com.capstone.ecommplatform.service.ScheduledTestDriveService;
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
 * REST controller for managing {@link com.capstone.ecommplatform.domain.ScheduledTestDrive}.
 */
@RestController
@RequestMapping("/api/scheduled-test-drives")
public class ScheduledTestDriveResource {

    private final Logger log = LoggerFactory.getLogger(ScheduledTestDriveResource.class);

    private static final String ENTITY_NAME = "scheduledTestDrive";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ScheduledTestDriveService scheduledTestDriveService;

    private final ScheduledTestDriveRepository scheduledTestDriveRepository;

    public ScheduledTestDriveResource(
        ScheduledTestDriveService scheduledTestDriveService,
        ScheduledTestDriveRepository scheduledTestDriveRepository
    ) {
        this.scheduledTestDriveService = scheduledTestDriveService;
        this.scheduledTestDriveRepository = scheduledTestDriveRepository;
    }

    /**
     * {@code POST  /scheduled-test-drives} : Create a new scheduledTestDrive.
     *
     * @param scheduledTestDrive the scheduledTestDrive to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new scheduledTestDrive, or with status {@code 400 (Bad Request)} if the scheduledTestDrive has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ScheduledTestDrive> createScheduledTestDrive(@Valid @RequestBody ScheduledTestDrive scheduledTestDrive)
        throws URISyntaxException {
        log.debug("REST request to save ScheduledTestDrive : {}", scheduledTestDrive);
        if (scheduledTestDrive.getId() != null) {
            throw new BadRequestAlertException("A new scheduledTestDrive cannot already have an ID", ENTITY_NAME, "idexists");
        }
        scheduledTestDrive = scheduledTestDriveService.save(scheduledTestDrive);
        return ResponseEntity.created(new URI("/api/scheduled-test-drives/" + scheduledTestDrive.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, scheduledTestDrive.getId().toString()))
            .body(scheduledTestDrive);
    }

    /**
     * {@code PUT  /scheduled-test-drives/:id} : Updates an existing scheduledTestDrive.
     *
     * @param id the id of the scheduledTestDrive to save.
     * @param scheduledTestDrive the scheduledTestDrive to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scheduledTestDrive,
     * or with status {@code 400 (Bad Request)} if the scheduledTestDrive is not valid,
     * or with status {@code 500 (Internal Server Error)} if the scheduledTestDrive couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ScheduledTestDrive> updateScheduledTestDrive(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ScheduledTestDrive scheduledTestDrive
    ) throws URISyntaxException {
        log.debug("REST request to update ScheduledTestDrive : {}, {}", id, scheduledTestDrive);
        if (scheduledTestDrive.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scheduledTestDrive.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scheduledTestDriveRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        scheduledTestDrive = scheduledTestDriveService.update(scheduledTestDrive);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, scheduledTestDrive.getId().toString()))
            .body(scheduledTestDrive);
    }

    /**
     * {@code PATCH  /scheduled-test-drives/:id} : Partial updates given fields of an existing scheduledTestDrive, field will ignore if it is null
     *
     * @param id the id of the scheduledTestDrive to save.
     * @param scheduledTestDrive the scheduledTestDrive to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scheduledTestDrive,
     * or with status {@code 400 (Bad Request)} if the scheduledTestDrive is not valid,
     * or with status {@code 404 (Not Found)} if the scheduledTestDrive is not found,
     * or with status {@code 500 (Internal Server Error)} if the scheduledTestDrive couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ScheduledTestDrive> partialUpdateScheduledTestDrive(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ScheduledTestDrive scheduledTestDrive
    ) throws URISyntaxException {
        log.debug("REST request to partial update ScheduledTestDrive partially : {}, {}", id, scheduledTestDrive);
        if (scheduledTestDrive.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scheduledTestDrive.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scheduledTestDriveRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ScheduledTestDrive> result = scheduledTestDriveService.partialUpdate(scheduledTestDrive);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, scheduledTestDrive.getId().toString())
        );
    }

    /**
     * {@code GET  /scheduled-test-drives} : get all the scheduledTestDrives.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of scheduledTestDrives in body.
     */
    @GetMapping("")
    public List<ScheduledTestDrive> getAllScheduledTestDrives() {
        log.debug("REST request to get all ScheduledTestDrives");
        return scheduledTestDriveService.findAll();
    }

    /**
     * {@code GET  /scheduled-test-drives/:id} : get the "id" scheduledTestDrive.
     *
     * @param id the id of the scheduledTestDrive to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the scheduledTestDrive, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ScheduledTestDrive> getScheduledTestDrive(@PathVariable("id") Long id) {
        log.debug("REST request to get ScheduledTestDrive : {}", id);
        Optional<ScheduledTestDrive> scheduledTestDrive = scheduledTestDriveService.findOne(id);
        return ResponseUtil.wrapOrNotFound(scheduledTestDrive);
    }

    /**
     * {@code DELETE  /scheduled-test-drives/:id} : delete the "id" scheduledTestDrive.
     *
     * @param id the id of the scheduledTestDrive to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScheduledTestDrive(@PathVariable("id") Long id) {
        log.debug("REST request to delete ScheduledTestDrive : {}", id);
        scheduledTestDriveService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
