package com.capstone.ecommplatform.web.rest;

import com.capstone.ecommplatform.domain.Dealer;
import com.capstone.ecommplatform.repository.DealerRepository;
import com.capstone.ecommplatform.service.DealerService;
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
 * REST controller for managing {@link com.capstone.ecommplatform.domain.Dealer}.
 */
@RestController
@RequestMapping("/api/dealers")
public class DealerResource {

    private final Logger log = LoggerFactory.getLogger(DealerResource.class);

    private static final String ENTITY_NAME = "dealer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DealerService dealerService;

    private final DealerRepository dealerRepository;

    public DealerResource(DealerService dealerService, DealerRepository dealerRepository) {
        this.dealerService = dealerService;
        this.dealerRepository = dealerRepository;
    }

    /**
     * {@code POST  /dealers} : Create a new dealer.
     *
     * @param dealer the dealer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dealer, or with status {@code 400 (Bad Request)} if the dealer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Dealer> createDealer(@Valid @RequestBody Dealer dealer) throws URISyntaxException {
        log.debug("REST request to save Dealer : {}", dealer);
        if (dealer.getId() != null) {
            throw new BadRequestAlertException("A new dealer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        dealer = dealerService.save(dealer);
        return ResponseEntity.created(new URI("/api/dealers/" + dealer.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, dealer.getId().toString()))
            .body(dealer);
    }

    /**
     * {@code PUT  /dealers/:id} : Updates an existing dealer.
     *
     * @param id the id of the dealer to save.
     * @param dealer the dealer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dealer,
     * or with status {@code 400 (Bad Request)} if the dealer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dealer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Dealer> updateDealer(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Dealer dealer
    ) throws URISyntaxException {
        log.debug("REST request to update Dealer : {}, {}", id, dealer);
        if (dealer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dealer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dealerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        dealer = dealerService.update(dealer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dealer.getId().toString()))
            .body(dealer);
    }

    /**
     * {@code PATCH  /dealers/:id} : Partial updates given fields of an existing dealer, field will ignore if it is null
     *
     * @param id the id of the dealer to save.
     * @param dealer the dealer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dealer,
     * or with status {@code 400 (Bad Request)} if the dealer is not valid,
     * or with status {@code 404 (Not Found)} if the dealer is not found,
     * or with status {@code 500 (Internal Server Error)} if the dealer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Dealer> partialUpdateDealer(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Dealer dealer
    ) throws URISyntaxException {
        log.debug("REST request to partial update Dealer partially : {}, {}", id, dealer);
        if (dealer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dealer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dealerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Dealer> result = dealerService.partialUpdate(dealer);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dealer.getId().toString())
        );
    }

    /**
     * {@code GET  /dealers} : get all the dealers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dealers in body.
     */
    @GetMapping("")
    public List<Dealer> getAllDealers() {
        log.debug("REST request to get all Dealers");
        return dealerService.findAll();
    }

    /**
     * {@code GET  /dealers/:id} : get the "id" dealer.
     *
     * @param id the id of the dealer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dealer, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Dealer> getDealer(@PathVariable("id") Long id) {
        log.debug("REST request to get Dealer : {}", id);
        Optional<Dealer> dealer = dealerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dealer);
    }

    /**
     * {@code DELETE  /dealers/:id} : delete the "id" dealer.
     *
     * @param id the id of the dealer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDealer(@PathVariable("id") Long id) {
        log.debug("REST request to delete Dealer : {}", id);
        dealerService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
