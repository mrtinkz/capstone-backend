package com.capstone.ecommplatform.service.impl;

import com.capstone.ecommplatform.domain.DocumentBlob;
import com.capstone.ecommplatform.repository.DocumentBlobRepository;
import com.capstone.ecommplatform.service.DocumentBlobService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.capstone.ecommplatform.domain.DocumentBlob}.
 */
@Service
@Transactional
public class DocumentBlobServiceImpl implements DocumentBlobService {

    private final Logger log = LoggerFactory.getLogger(DocumentBlobServiceImpl.class);

    private final DocumentBlobRepository documentBlobRepository;

    public DocumentBlobServiceImpl(DocumentBlobRepository documentBlobRepository) {
        this.documentBlobRepository = documentBlobRepository;
    }

    @Override
    public DocumentBlob save(DocumentBlob documentBlob) {
        log.debug("Request to save DocumentBlob : {}", documentBlob);
        return documentBlobRepository.save(documentBlob);
    }

    @Override
    public DocumentBlob update(DocumentBlob documentBlob) {
        log.debug("Request to update DocumentBlob : {}", documentBlob);
        return documentBlobRepository.save(documentBlob);
    }

    @Override
    public Optional<DocumentBlob> partialUpdate(DocumentBlob documentBlob) {
        log.debug("Request to partially update DocumentBlob : {}", documentBlob);

        return documentBlobRepository
            .findById(documentBlob.getId())
            .map(existingDocumentBlob -> {
                if (documentBlob.getPostPurchaseActivityId() != null) {
                    existingDocumentBlob.setPostPurchaseActivityId(documentBlob.getPostPurchaseActivityId());
                }
                if (documentBlob.getName() != null) {
                    existingDocumentBlob.setName(documentBlob.getName());
                }
                if (documentBlob.getMimeType() != null) {
                    existingDocumentBlob.setMimeType(documentBlob.getMimeType());
                }
                if (documentBlob.getData() != null) {
                    existingDocumentBlob.setData(documentBlob.getData());
                }
                if (documentBlob.getDataContentType() != null) {
                    existingDocumentBlob.setDataContentType(documentBlob.getDataContentType());
                }

                return existingDocumentBlob;
            })
            .map(documentBlobRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentBlob> findAll() {
        log.debug("Request to get all DocumentBlobs");
        return documentBlobRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentBlob> findOne(Long id) {
        log.debug("Request to get DocumentBlob : {}", id);
        return documentBlobRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DocumentBlob : {}", id);
        documentBlobRepository.deleteById(id);
    }
}
