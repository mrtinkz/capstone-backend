package com.capstone.ecommplatform.domain;

import static com.capstone.ecommplatform.domain.DocumentBlobTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.capstone.ecommplatform.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentBlobTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentBlob.class);
        DocumentBlob documentBlob1 = getDocumentBlobSample1();
        DocumentBlob documentBlob2 = new DocumentBlob();
        assertThat(documentBlob1).isNotEqualTo(documentBlob2);

        documentBlob2.setId(documentBlob1.getId());
        assertThat(documentBlob1).isEqualTo(documentBlob2);

        documentBlob2 = getDocumentBlobSample2();
        assertThat(documentBlob1).isNotEqualTo(documentBlob2);
    }
}
