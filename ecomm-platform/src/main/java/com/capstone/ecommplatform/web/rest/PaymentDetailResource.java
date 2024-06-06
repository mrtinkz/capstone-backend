package com.capstone.ecommplatform.web.rest;

import com.capstone.ecommplatform.domain.PaymentDetail;
import com.capstone.ecommplatform.repository.PaymentDetailRepository;
import com.capstone.ecommplatform.service.PaymentDetailService;
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
 * REST controller for managing {@link com.capstone.ecommplatform.domain.PaymentDetail}.
 */
@RestController
@RequestMapping("/api/payment-details")
public class PaymentDetailResource {

    private final Logger log = LoggerFactory.getLogger(PaymentDetailResource.class);

    private static final String ENTITY_NAME = "paymentDetail";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentDetailService paymentDetailService;

    private final PaymentDetailRepository paymentDetailRepository;

    public PaymentDetailResource(PaymentDetailService paymentDetailService, PaymentDetailRepository paymentDetailRepository) {
        this.paymentDetailService = paymentDetailService;
        this.paymentDetailRepository = paymentDetailRepository;
    }

    /**
     * {@code POST  /payment-details} : Create a new paymentDetail.
     *
     * @param paymentDetail the paymentDetail to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentDetail, or with status {@code 400 (Bad Request)} if the paymentDetail has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PaymentDetail> createPaymentDetail(@Valid @RequestBody PaymentDetail paymentDetail) throws URISyntaxException {
        log.debug("REST request to save PaymentDetail : {}", paymentDetail);
        if (paymentDetail.getId() != null) {
            throw new BadRequestAlertException("A new paymentDetail cannot already have an ID", ENTITY_NAME, "idexists");
        }
        paymentDetail = paymentDetailService.save(paymentDetail);
        return ResponseEntity.created(new URI("/api/payment-details/" + paymentDetail.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, paymentDetail.getId().toString()))
            .body(paymentDetail);
    }

    /**
     * {@code PUT  /payment-details/:id} : Updates an existing paymentDetail.
     *
     * @param id the id of the paymentDetail to save.
     * @param paymentDetail the paymentDetail to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentDetail,
     * or with status {@code 400 (Bad Request)} if the paymentDetail is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentDetail couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PaymentDetail> updatePaymentDetail(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PaymentDetail paymentDetail
    ) throws URISyntaxException {
        log.debug("REST request to update PaymentDetail : {}, {}", id, paymentDetail);
        if (paymentDetail.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentDetail.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentDetailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        paymentDetail = paymentDetailService.update(paymentDetail);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, paymentDetail.getId().toString()))
            .body(paymentDetail);
    }

    /**
     * {@code PATCH  /payment-details/:id} : Partial updates given fields of an existing paymentDetail, field will ignore if it is null
     *
     * @param id the id of the paymentDetail to save.
     * @param paymentDetail the paymentDetail to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentDetail,
     * or with status {@code 400 (Bad Request)} if the paymentDetail is not valid,
     * or with status {@code 404 (Not Found)} if the paymentDetail is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentDetail couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaymentDetail> partialUpdatePaymentDetail(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PaymentDetail paymentDetail
    ) throws URISyntaxException {
        log.debug("REST request to partial update PaymentDetail partially : {}, {}", id, paymentDetail);
        if (paymentDetail.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentDetail.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentDetailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentDetail> result = paymentDetailService.partialUpdate(paymentDetail);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, paymentDetail.getId().toString())
        );
    }

    /**
     * {@code GET  /payment-details} : get all the paymentDetails.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentDetails in body.
     */
    @GetMapping("")
    public List<PaymentDetail> getAllPaymentDetails() {
        log.debug("REST request to get all PaymentDetails");
        return paymentDetailService.findAll();
    }

    /**
     * {@code GET  /payment-details/:id} : get the "id" paymentDetail.
     *
     * @param id the id of the paymentDetail to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentDetail, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentDetail> getPaymentDetail(@PathVariable("id") Long id) {
        log.debug("REST request to get PaymentDetail : {}", id);
        Optional<PaymentDetail> paymentDetail = paymentDetailService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentDetail);
    }

    /**
     * {@code DELETE  /payment-details/:id} : delete the "id" paymentDetail.
     *
     * @param id the id of the paymentDetail to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentDetail(@PathVariable("id") Long id) {
        log.debug("REST request to delete PaymentDetail : {}", id);
        paymentDetailService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
