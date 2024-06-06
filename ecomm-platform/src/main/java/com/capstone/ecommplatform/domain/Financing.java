package com.capstone.ecommplatform.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A Financing.
 */
@Entity
@Table(name = "financing")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Financing implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "provider", nullable = false)
    private String provider;

    @NotNull
    @Column(name = "interest_rate", nullable = false)
    private Float interestRate;

    @NotNull
    @Column(name = "loan_term", nullable = false)
    private Integer loanTerm;

    @Column(name = "down_payment", precision = 21, scale = 2)
    private BigDecimal downPayment;

    @Column(name = "order_id")
    private Long orderId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Financing id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProvider() {
        return this.provider;
    }

    public Financing provider(String provider) {
        this.setProvider(provider);
        return this;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Float getInterestRate() {
        return this.interestRate;
    }

    public Financing interestRate(Float interestRate) {
        this.setInterestRate(interestRate);
        return this;
    }

    public void setInterestRate(Float interestRate) {
        this.interestRate = interestRate;
    }

    public Integer getLoanTerm() {
        return this.loanTerm;
    }

    public Financing loanTerm(Integer loanTerm) {
        this.setLoanTerm(loanTerm);
        return this;
    }

    public void setLoanTerm(Integer loanTerm) {
        this.loanTerm = loanTerm;
    }

    public BigDecimal getDownPayment() {
        return this.downPayment;
    }

    public Financing downPayment(BigDecimal downPayment) {
        this.setDownPayment(downPayment);
        return this;
    }

    public void setDownPayment(BigDecimal downPayment) {
        this.downPayment = downPayment;
    }

    public Long getOrderId() {
        return this.orderId;
    }

    public Financing orderId(Long orderId) {
        this.setOrderId(orderId);
        return this;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Financing)) {
            return false;
        }
        return getId() != null && getId().equals(((Financing) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Financing{" +
            "id=" + getId() +
            ", provider='" + getProvider() + "'" +
            ", interestRate=" + getInterestRate() +
            ", loanTerm=" + getLoanTerm() +
            ", downPayment=" + getDownPayment() +
            ", orderId=" + getOrderId() +
            "}";
    }
}
