package com.capstone.ecommplatform.web.rest;

import com.capstone.ecommplatform.domain.VehicleOptions;
import com.capstone.ecommplatform.repository.VehicleOptionsRepository;
import com.capstone.ecommplatform.service.VehicleOptionsService;
import com.capstone.ecommplatform.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.capstone.ecommplatform.domain.VehicleOptions}.
 */
@RestController
@RequestMapping("/api/vehicle-options")
public class VehicleOptionsResource {

    private final Logger log = LoggerFactory.getLogger(VehicleOptionsResource.class);

    private static final String ENTITY_NAME = "vehicleOptions";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VehicleOptionsService vehicleOptionsService;

    private final VehicleOptionsRepository vehicleOptionsRepository;

    public VehicleOptionsResource(VehicleOptionsService vehicleOptionsService, VehicleOptionsRepository vehicleOptionsRepository) {
        this.vehicleOptionsService = vehicleOptionsService;
        this.vehicleOptionsRepository = vehicleOptionsRepository;
    }

    /**
     * {@code POST  /vehicle-options} : Create a new vehicleOptions.
     *
     * @param vehicleOptions the vehicleOptions to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vehicleOptions, or with status {@code 400 (Bad Request)} if the vehicleOptions has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<VehicleOptions> createVehicleOptions(@RequestBody VehicleOptions vehicleOptions) throws URISyntaxException {
        log.debug("REST request to save VehicleOptions : {}", vehicleOptions);
        if (vehicleOptions.getId() != null) {
            throw new BadRequestAlertException("A new vehicleOptions cannot already have an ID", ENTITY_NAME, "idexists");
        }
        vehicleOptions = vehicleOptionsService.save(vehicleOptions);
        return ResponseEntity.created(new URI("/api/vehicle-options/" + vehicleOptions.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, vehicleOptions.getId().toString()))
            .body(vehicleOptions);
    }

    /**
     * {@code PUT  /vehicle-options/:id} : Updates an existing vehicleOptions.
     *
     * @param id the id of the vehicleOptions to save.
     * @param vehicleOptions the vehicleOptions to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicleOptions,
     * or with status {@code 400 (Bad Request)} if the vehicleOptions is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vehicleOptions couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VehicleOptions> updateVehicleOptions(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VehicleOptions vehicleOptions
    ) throws URISyntaxException {
        log.debug("REST request to update VehicleOptions : {}, {}", id, vehicleOptions);
        if (vehicleOptions.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicleOptions.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehicleOptionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        vehicleOptions = vehicleOptionsService.update(vehicleOptions);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vehicleOptions.getId().toString()))
            .body(vehicleOptions);
    }

    /**
     * {@code PATCH  /vehicle-options/:id} : Partial updates given fields of an existing vehicleOptions, field will ignore if it is null
     *
     * @param id the id of the vehicleOptions to save.
     * @param vehicleOptions the vehicleOptions to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicleOptions,
     * or with status {@code 400 (Bad Request)} if the vehicleOptions is not valid,
     * or with status {@code 404 (Not Found)} if the vehicleOptions is not found,
     * or with status {@code 500 (Internal Server Error)} if the vehicleOptions couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VehicleOptions> partialUpdateVehicleOptions(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VehicleOptions vehicleOptions
    ) throws URISyntaxException {
        log.debug("REST request to partial update VehicleOptions partially : {}, {}", id, vehicleOptions);
        if (vehicleOptions.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicleOptions.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehicleOptionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VehicleOptions> result = vehicleOptionsService.partialUpdate(vehicleOptions);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vehicleOptions.getId().toString())
        );
    }

    /**
     * {@code GET  /vehicle-options} : get all the vehicleOptions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vehicleOptions in body.
     */
    @GetMapping("")
    public List<VehicleOptions> getAllVehicleOptions() {
        log.debug("REST request to get all VehicleOptions");
        return vehicleOptionsService.findAll();
    }

    /**
     * {@code GET  /vehicle-options/:id} : get the "id" vehicleOptions.
     *
     * @param id the id of the vehicleOptions to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vehicleOptions, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VehicleOptions> getVehicleOptions(@PathVariable("id") Long id) {
        log.debug("REST request to get VehicleOptions : {}", id);
        Optional<VehicleOptions> vehicleOptions = vehicleOptionsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vehicleOptions);
    }

    /**
     * {@code DELETE  /vehicle-options/:id} : delete the "id" vehicleOptions.
     *
     * @param id the id of the vehicleOptions to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicleOptions(@PathVariable("id") Long id) {
        log.debug("REST request to delete VehicleOptions : {}", id);
        vehicleOptionsService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
