package com.capstone.ecommplatform.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Vehicle.
 */
@Entity
@Table(name = "vehicle")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Vehicle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "vin", nullable = false)
    private String vin;

    @NotNull
    @Column(name = "make", nullable = false)
    private String make;

    @NotNull
    @Column(name = "model", nullable = false)
    private String model;

    @NotNull
    @Column(name = "model_year", nullable = false)
    private Integer modelYear;

    @NotNull
    @Column(name = "dealer_id", nullable = false)
    private Long dealerId;

    @Column(name = "pricing_summary_id")
    private Long pricingSummaryId;

    @Column(name = "vehicle_options_id")
    private Long vehicleOptionsId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Vehicle id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVin() {
        return this.vin;
    }

    public Vehicle vin(String vin) {
        this.setVin(vin);
        return this;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getMake() {
        return this.make;
    }

    public Vehicle make(String make) {
        this.setMake(make);
        return this;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return this.model;
    }

    public Vehicle model(String model) {
        this.setModel(model);
        return this;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getModelYear() {
        return this.modelYear;
    }

    public Vehicle modelYear(Integer modelYear) {
        this.setModelYear(modelYear);
        return this;
    }

    public void setModelYear(Integer modelYear) {
        this.modelYear = modelYear;
    }

    public Long getDealerId() {
        return this.dealerId;
    }

    public Vehicle dealerId(Long dealerId) {
        this.setDealerId(dealerId);
        return this;
    }

    public void setDealerId(Long dealerId) {
        this.dealerId = dealerId;
    }

    public Long getPricingSummaryId() {
        return this.pricingSummaryId;
    }

    public Vehicle pricingSummaryId(Long pricingSummaryId) {
        this.setPricingSummaryId(pricingSummaryId);
        return this;
    }

    public void setPricingSummaryId(Long pricingSummaryId) {
        this.pricingSummaryId = pricingSummaryId;
    }

    public Long getVehicleOptionsId() {
        return this.vehicleOptionsId;
    }

    public Vehicle vehicleOptionsId(Long vehicleOptionsId) {
        this.setVehicleOptionsId(vehicleOptionsId);
        return this;
    }

    public void setVehicleOptionsId(Long vehicleOptionsId) {
        this.vehicleOptionsId = vehicleOptionsId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vehicle)) {
            return false;
        }
        return getId() != null && getId().equals(((Vehicle) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vehicle{" +
            "id=" + getId() +
            ", vin='" + getVin() + "'" +
            ", make='" + getMake() + "'" +
            ", model='" + getModel() + "'" +
            ", modelYear=" + getModelYear() +
            ", dealerId=" + getDealerId() +
            ", pricingSummaryId=" + getPricingSummaryId() +
            ", vehicleOptionsId=" + getVehicleOptionsId() +
            "}";
    }
}
