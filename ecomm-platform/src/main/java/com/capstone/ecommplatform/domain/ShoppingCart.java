package com.capstone.ecommplatform.domain;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A ShoppingCart.
 */
@Entity
@Table(name = "shopping_cart")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ShoppingCart implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "shopper_id")
    private Long shopperId;

    @Column(name = "vehicle_id")
    private Long vehicleId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ShoppingCart id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getShopperId() {
        return this.shopperId;
    }

    public ShoppingCart shopperId(Long shopperId) {
        this.setShopperId(shopperId);
        return this;
    }

    public void setShopperId(Long shopperId) {
        this.shopperId = shopperId;
    }

    public Long getVehicleId() {
        return this.vehicleId;
    }

    public ShoppingCart vehicleId(Long vehicleId) {
        this.setVehicleId(vehicleId);
        return this;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShoppingCart)) {
            return false;
        }
        return getId() != null && getId().equals(((ShoppingCart) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShoppingCart{" +
            "id=" + getId() +
            ", shopperId=" + getShopperId() +
            ", vehicleId=" + getVehicleId() +
            "}";
    }
}
