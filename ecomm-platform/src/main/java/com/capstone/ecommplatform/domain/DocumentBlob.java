package com.capstone.ecommplatform.domain;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A DocumentBlob.
 */
@Entity
@Table(name = "document_blob")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentBlob implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "post_purchase_activity_id")
    private Long postPurchaseActivityId;

    @Column(name = "name")
    private String name;

    @Column(name = "mime_type")
    private String mimeType;

    @Lob
    @Column(name = "data")
    private byte[] data;

    @Column(name = "data_content_type")
    private String dataContentType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DocumentBlob id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPostPurchaseActivityId() {
        return this.postPurchaseActivityId;
    }

    public DocumentBlob postPurchaseActivityId(Long postPurchaseActivityId) {
        this.setPostPurchaseActivityId(postPurchaseActivityId);
        return this;
    }

    public void setPostPurchaseActivityId(Long postPurchaseActivityId) {
        this.postPurchaseActivityId = postPurchaseActivityId;
    }

    public String getName() {
        return this.name;
    }

    public DocumentBlob name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public DocumentBlob mimeType(String mimeType) {
        this.setMimeType(mimeType);
        return this;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public byte[] getData() {
        return this.data;
    }

    public DocumentBlob data(byte[] data) {
        this.setData(data);
        return this;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getDataContentType() {
        return this.dataContentType;
    }

    public DocumentBlob dataContentType(String dataContentType) {
        this.dataContentType = dataContentType;
        return this;
    }

    public void setDataContentType(String dataContentType) {
        this.dataContentType = dataContentType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentBlob)) {
            return false;
        }
        return getId() != null && getId().equals(((DocumentBlob) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentBlob{" +
            "id=" + getId() +
            ", postPurchaseActivityId=" + getPostPurchaseActivityId() +
            ", name='" + getName() + "'" +
            ", mimeType='" + getMimeType() + "'" +
            ", data='" + getData() + "'" +
            ", dataContentType='" + getDataContentType() + "'" +
            "}";
    }
}
