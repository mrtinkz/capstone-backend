package com.capstone.ecommplatform.domain;

import com.capstone.ecommplatform.domain.enumeration.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Order.
 */
@Entity
@Table(name = "jhi_order")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "shopper_id")
    private Long shopperId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Column(name = "scheduled_delivery_date")
    private LocalDate scheduledDeliveryDate;

    @NotNull
    @Column(name = "vehicle_id", nullable = false)
    private Long vehicleId;

    @NotNull
    @Column(name = "dealer_id", nullable = false)
    private Long dealerId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Order id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getShopperId() {
        return this.shopperId;
    }

    public Order shopperId(Long shopperId) {
        this.setShopperId(shopperId);
        return this;
    }

    public void setShopperId(Long shopperId) {
        this.shopperId = shopperId;
    }

    public OrderStatus getStatus() {
        return this.status;
    }

    public Order status(OrderStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDate getScheduledDeliveryDate() {
        return this.scheduledDeliveryDate;
    }

    public Order scheduledDeliveryDate(LocalDate scheduledDeliveryDate) {
        this.setScheduledDeliveryDate(scheduledDeliveryDate);
        return this;
    }

    public void setScheduledDeliveryDate(LocalDate scheduledDeliveryDate) {
        this.scheduledDeliveryDate = scheduledDeliveryDate;
    }

    public Long getVehicleId() {
        return this.vehicleId;
    }

    public Order vehicleId(Long vehicleId) {
        this.setVehicleId(vehicleId);
        return this;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Long getDealerId() {
        return this.dealerId;
    }

    public Order dealerId(Long dealerId) {
        this.setDealerId(dealerId);
        return this;
    }

    public void setDealerId(Long dealerId) {
        this.dealerId = dealerId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        return getId() != null && getId().equals(((Order) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Order{" +
            "id=" + getId() +
            ", shopperId=" + getShopperId() +
            ", status='" + getStatus() + "'" +
            ", scheduledDeliveryDate='" + getScheduledDeliveryDate() + "'" +
            ", vehicleId=" + getVehicleId() +
            ", dealerId=" + getDealerId() +
            "}";
    }
}
