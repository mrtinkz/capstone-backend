package com.capstone.ecommplatform.domain;

import com.capstone.ecommplatform.domain.enumeration.Color;
import com.capstone.ecommplatform.domain.enumeration.DrivetrainType;
import com.capstone.ecommplatform.domain.enumeration.EngineType;
import com.capstone.ecommplatform.domain.enumeration.TransmissionType;
import com.capstone.ecommplatform.domain.enumeration.Trim;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A VehicleOptions.
 */
@Entity
@Table(name = "vehicle_options")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VehicleOptions implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "estimated_mileage")
    private Integer estimatedMileage;

    @Enumerated(EnumType.STRING)
    @Column(name = "engine")
    private EngineType engine;

    @Enumerated(EnumType.STRING)
    @Column(name = "drivetrain")
    private DrivetrainType drivetrain;

    @Enumerated(EnumType.STRING)
    @Column(name = "transmission")
    private TransmissionType transmission;

    @Enumerated(EnumType.STRING)
    @Column(name = "trim")
    private Trim trim;

    @Enumerated(EnumType.STRING)
    @Column(name = "color")
    private Color color;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public VehicleOptions id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEstimatedMileage() {
        return this.estimatedMileage;
    }

    public VehicleOptions estimatedMileage(Integer estimatedMileage) {
        this.setEstimatedMileage(estimatedMileage);
        return this;
    }

    public void setEstimatedMileage(Integer estimatedMileage) {
        this.estimatedMileage = estimatedMileage;
    }

    public EngineType getEngine() {
        return this.engine;
    }

    public VehicleOptions engine(EngineType engine) {
        this.setEngine(engine);
        return this;
    }

    public void setEngine(EngineType engine) {
        this.engine = engine;
    }

    public DrivetrainType getDrivetrain() {
        return this.drivetrain;
    }

    public VehicleOptions drivetrain(DrivetrainType drivetrain) {
        this.setDrivetrain(drivetrain);
        return this;
    }

    public void setDrivetrain(DrivetrainType drivetrain) {
        this.drivetrain = drivetrain;
    }

    public TransmissionType getTransmission() {
        return this.transmission;
    }

    public VehicleOptions transmission(TransmissionType transmission) {
        this.setTransmission(transmission);
        return this;
    }

    public void setTransmission(TransmissionType transmission) {
        this.transmission = transmission;
    }

    public Trim getTrim() {
        return this.trim;
    }

    public VehicleOptions trim(Trim trim) {
        this.setTrim(trim);
        return this;
    }

    public void setTrim(Trim trim) {
        this.trim = trim;
    }

    public Color getColor() {
        return this.color;
    }

    public VehicleOptions color(Color color) {
        this.setColor(color);
        return this;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VehicleOptions)) {
            return false;
        }
        return getId() != null && getId().equals(((VehicleOptions) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VehicleOptions{" +
            "id=" + getId() +
            ", estimatedMileage=" + getEstimatedMileage() +
            ", engine='" + getEngine() + "'" +
            ", drivetrain='" + getDrivetrain() + "'" +
            ", transmission='" + getTransmission() + "'" +
            ", trim='" + getTrim() + "'" +
            ", color='" + getColor() + "'" +
            "}";
    }
}
