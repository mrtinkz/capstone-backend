package com.capstone.ecommplatform.domain;

import com.capstone.ecommplatform.domain.enumeration.PostPurchaseActivityStatus;
import com.capstone.ecommplatform.domain.enumeration.PostPurchaseActivityType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A PostPurchaseActivity.
 */
@Entity
@Table(name = "post_purchase_activity")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PostPurchaseActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "order_id")
    private Long orderId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "activity_type", nullable = false)
    private PostPurchaseActivityType activityType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PostPurchaseActivityStatus status;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PostPurchaseActivity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return this.orderId;
    }

    public PostPurchaseActivity orderId(Long orderId) {
        this.setOrderId(orderId);
        return this;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public PostPurchaseActivityType getActivityType() {
        return this.activityType;
    }

    public PostPurchaseActivity activityType(PostPurchaseActivityType activityType) {
        this.setActivityType(activityType);
        return this;
    }

    public void setActivityType(PostPurchaseActivityType activityType) {
        this.activityType = activityType;
    }

    public PostPurchaseActivityStatus getStatus() {
        return this.status;
    }

    public PostPurchaseActivity status(PostPurchaseActivityStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(PostPurchaseActivityStatus status) {
        this.status = status;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PostPurchaseActivity)) {
            return false;
        }
        return getId() != null && getId().equals(((PostPurchaseActivity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PostPurchaseActivity{" +
            "id=" + getId() +
            ", orderId=" + getOrderId() +
            ", activityType='" + getActivityType() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
