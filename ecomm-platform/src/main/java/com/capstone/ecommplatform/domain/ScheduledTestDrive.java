package com.capstone.ecommplatform.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A ScheduledTestDrive.
 */
@Entity
@Table(name = "scheduled_test_drive")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ScheduledTestDrive implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "shopper_id")
    private Long shopperId;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ScheduledTestDrive id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getShopperId() {
        return this.shopperId;
    }

    public ScheduledTestDrive shopperId(Long shopperId) {
        this.setShopperId(shopperId);
        return this;
    }

    public void setShopperId(Long shopperId) {
        this.shopperId = shopperId;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public ScheduledTestDrive date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ScheduledTestDrive)) {
            return false;
        }
        return getId() != null && getId().equals(((ScheduledTestDrive) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ScheduledTestDrive{" +
            "id=" + getId() +
            ", shopperId=" + getShopperId() +
            ", date='" + getDate() + "'" +
            "}";
    }
}
