package com.capstone.ecommplatform.web.rest;

import com.capstone.ecommplatform.domain.Shopper;
import com.capstone.ecommplatform.repository.ShopperRepository;
import com.capstone.ecommplatform.service.ShopperService;
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
 * REST controller for managing {@link com.capstone.ecommplatform.domain.Shopper}.
 */
@RestController
@RequestMapping("/api/shoppers")
public class ShopperResource {

    private final Logger log = LoggerFactory.getLogger(ShopperResource.class);

    private static final String ENTITY_NAME = "shopper";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShopperService shopperService;

    private final ShopperRepository shopperRepository;

    public ShopperResource(ShopperService shopperService, ShopperRepository shopperRepository) {
        this.shopperService = shopperService;
        this.shopperRepository = shopperRepository;
    }

    /**
     * {@code POST  /shoppers} : Create a new shopper.
     *
     * @param shopper the shopper to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shopper, or with status {@code 400 (Bad Request)} if the shopper has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Shopper> createShopper(@RequestBody Shopper shopper) throws URISyntaxException {
        log.debug("REST request to save Shopper : {}", shopper);
        if (shopper.getId() != null) {
            throw new BadRequestAlertException("A new shopper cannot already have an ID", ENTITY_NAME, "idexists");
        }
        shopper = shopperService.save(shopper);
        return ResponseEntity.created(new URI("/api/shoppers/" + shopper.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, shopper.getId().toString()))
            .body(shopper);
    }

    /**
     * {@code PUT  /shoppers/:id} : Updates an existing shopper.
     *
     * @param id the id of the shopper to save.
     * @param shopper the shopper to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shopper,
     * or with status {@code 400 (Bad Request)} if the shopper is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shopper couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Shopper> updateShopper(@PathVariable(value = "id", required = false) final Long id, @RequestBody Shopper shopper)
        throws URISyntaxException {
        log.debug("REST request to update Shopper : {}, {}", id, shopper);
        if (shopper.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shopper.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shopperRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        shopper = shopperService.update(shopper);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, shopper.getId().toString()))
            .body(shopper);
    }

    /**
     * {@code PATCH  /shoppers/:id} : Partial updates given fields of an existing shopper, field will ignore if it is null
     *
     * @param id the id of the shopper to save.
     * @param shopper the shopper to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shopper,
     * or with status {@code 400 (Bad Request)} if the shopper is not valid,
     * or with status {@code 404 (Not Found)} if the shopper is not found,
     * or with status {@code 500 (Internal Server Error)} if the shopper couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Shopper> partialUpdateShopper(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Shopper shopper
    ) throws URISyntaxException {
        log.debug("REST request to partial update Shopper partially : {}, {}", id, shopper);
        if (shopper.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shopper.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shopperRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Shopper> result = shopperService.partialUpdate(shopper);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, shopper.getId().toString())
        );
    }

    /**
     * {@code GET  /shoppers} : get all the shoppers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shoppers in body.
     */
    @GetMapping("")
    public List<Shopper> getAllShoppers() {
        log.debug("REST request to get all Shoppers");
        return shopperService.findAll();
    }

    /**
     * {@code GET  /shoppers/:id} : get the "id" shopper.
     *
     * @param id the id of the shopper to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shopper, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Shopper> getShopper(@PathVariable("id") Long id) {
        log.debug("REST request to get Shopper : {}", id);
        Optional<Shopper> shopper = shopperService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shopper);
    }

    /**
     * {@code DELETE  /shoppers/:id} : delete the "id" shopper.
     *
     * @param id the id of the shopper to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShopper(@PathVariable("id") Long id) {
        log.debug("REST request to delete Shopper : {}", id);
        shopperService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
