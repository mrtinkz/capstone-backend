package com.capstone.ecommplatform.web.rest;

import com.capstone.ecommplatform.domain.DocumentBlob;
import com.capstone.ecommplatform.repository.DocumentBlobRepository;
import com.capstone.ecommplatform.service.DocumentBlobService;
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
 * REST controller for managing {@link com.capstone.ecommplatform.domain.DocumentBlob}.
 */
@RestController
@RequestMapping("/api/document-blobs")
public class DocumentBlobResource {

    private final Logger log = LoggerFactory.getLogger(DocumentBlobResource.class);

    private static final String ENTITY_NAME = "documentBlob";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentBlobService documentBlobService;

    private final DocumentBlobRepository documentBlobRepository;

    public DocumentBlobResource(DocumentBlobService documentBlobService, DocumentBlobRepository documentBlobRepository) {
        this.documentBlobService = documentBlobService;
        this.documentBlobRepository = documentBlobRepository;
    }

    /**
     * {@code POST  /document-blobs} : Create a new documentBlob.
     *
     * @param documentBlob the documentBlob to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentBlob, or with status {@code 400 (Bad Request)} if the documentBlob has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DocumentBlob> createDocumentBlob(@RequestBody DocumentBlob documentBlob) throws URISyntaxException {
        log.debug("REST request to save DocumentBlob : {}", documentBlob);
        if (documentBlob.getId() != null) {
            throw new BadRequestAlertException("A new documentBlob cannot already have an ID", ENTITY_NAME, "idexists");
        }
        documentBlob = documentBlobService.save(documentBlob);
        return ResponseEntity.created(new URI("/api/document-blobs/" + documentBlob.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, documentBlob.getId().toString()))
            .body(documentBlob);
    }

    /**
     * {@code PUT  /document-blobs/:id} : Updates an existing documentBlob.
     *
     * @param id the id of the documentBlob to save.
     * @param documentBlob the documentBlob to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentBlob,
     * or with status {@code 400 (Bad Request)} if the documentBlob is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentBlob couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DocumentBlob> updateDocumentBlob(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocumentBlob documentBlob
    ) throws URISyntaxException {
        log.debug("REST request to update DocumentBlob : {}, {}", id, documentBlob);
        if (documentBlob.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentBlob.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentBlobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        documentBlob = documentBlobService.update(documentBlob);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, documentBlob.getId().toString()))
            .body(documentBlob);
    }

    /**
     * {@code PATCH  /document-blobs/:id} : Partial updates given fields of an existing documentBlob, field will ignore if it is null
     *
     * @param id the id of the documentBlob to save.
     * @param documentBlob the documentBlob to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentBlob,
     * or with status {@code 400 (Bad Request)} if the documentBlob is not valid,
     * or with status {@code 404 (Not Found)} if the documentBlob is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentBlob couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DocumentBlob> partialUpdateDocumentBlob(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocumentBlob documentBlob
    ) throws URISyntaxException {
        log.debug("REST request to partial update DocumentBlob partially : {}, {}", id, documentBlob);
        if (documentBlob.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentBlob.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentBlobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocumentBlob> result = documentBlobService.partialUpdate(documentBlob);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, documentBlob.getId().toString())
        );
    }

    /**
     * {@code GET  /document-blobs} : get all the documentBlobs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentBlobs in body.
     */
    @GetMapping("")
    public List<DocumentBlob> getAllDocumentBlobs() {
        log.debug("REST request to get all DocumentBlobs");
        return documentBlobService.findAll();
    }

    /**
     * {@code GET  /document-blobs/:id} : get the "id" documentBlob.
     *
     * @param id the id of the documentBlob to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentBlob, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DocumentBlob> getDocumentBlob(@PathVariable("id") Long id) {
        log.debug("REST request to get DocumentBlob : {}", id);
        Optional<DocumentBlob> documentBlob = documentBlobService.findOne(id);
        return ResponseUtil.wrapOrNotFound(documentBlob);
    }

    /**
     * {@code DELETE  /document-blobs/:id} : delete the "id" documentBlob.
     *
     * @param id the id of the documentBlob to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocumentBlob(@PathVariable("id") Long id) {
        log.debug("REST request to delete DocumentBlob : {}", id);
        documentBlobService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
