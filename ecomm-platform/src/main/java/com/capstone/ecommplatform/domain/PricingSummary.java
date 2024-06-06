package com.capstone.ecommplatform.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A PricingSummary.
 */
@Entity
@Table(name = "pricing_summary")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PricingSummary implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "msrp", precision = 21, scale = 2, nullable = false)
    private BigDecimal msrp;

    @NotNull
    @Column(name = "taxes_and_fees", precision = 21, scale = 2, nullable = false)
    private BigDecimal taxesAndFees;

    @Column(name = "incentives", precision = 21, scale = 2)
    private BigDecimal incentives;

    @Column(name = "trade_in_estimate", precision = 21, scale = 2)
    private BigDecimal tradeInEstimate;

    @Column(name = "subscription_services", precision = 21, scale = 2)
    private BigDecimal subscriptionServices;

    @Column(name = "protection_plan", precision = 21, scale = 2)
    private BigDecimal protectionPlan;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PricingSummary id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMsrp() {
        return this.msrp;
    }

    public PricingSummary msrp(BigDecimal msrp) {
        this.setMsrp(msrp);
        return this;
    }

    public void setMsrp(BigDecimal msrp) {
        this.msrp = msrp;
    }

    public BigDecimal getTaxesAndFees() {
        return this.taxesAndFees;
    }

    public PricingSummary taxesAndFees(BigDecimal taxesAndFees) {
        this.setTaxesAndFees(taxesAndFees);
        return this;
    }

    public void setTaxesAndFees(BigDecimal taxesAndFees) {
        this.taxesAndFees = taxesAndFees;
    }

    public BigDecimal getIncentives() {
        return this.incentives;
    }

    public PricingSummary incentives(BigDecimal incentives) {
        this.setIncentives(incentives);
        return this;
    }

    public void setIncentives(BigDecimal incentives) {
        this.incentives = incentives;
    }

    public BigDecimal getTradeInEstimate() {
        return this.tradeInEstimate;
    }

    public PricingSummary tradeInEstimate(BigDecimal tradeInEstimate) {
        this.setTradeInEstimate(tradeInEstimate);
        return this;
    }

    public void setTradeInEstimate(BigDecimal tradeInEstimate) {
        this.tradeInEstimate = tradeInEstimate;
    }

    public BigDecimal getSubscriptionServices() {
        return this.subscriptionServices;
    }

    public PricingSummary subscriptionServices(BigDecimal subscriptionServices) {
        this.setSubscriptionServices(subscriptionServices);
        return this;
    }

    public void setSubscriptionServices(BigDecimal subscriptionServices) {
        this.subscriptionServices = subscriptionServices;
    }

    public BigDecimal getProtectionPlan() {
        return this.protectionPlan;
    }

    public PricingSummary protectionPlan(BigDecimal protectionPlan) {
        this.setProtectionPlan(protectionPlan);
        return this;
    }

    public void setProtectionPlan(BigDecimal protectionPlan) {
        this.protectionPlan = protectionPlan;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PricingSummary)) {
            return false;
        }
        return getId() != null && getId().equals(((PricingSummary) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PricingSummary{" +
            "id=" + getId() +
            ", msrp=" + getMsrp() +
            ", taxesAndFees=" + getTaxesAndFees() +
            ", incentives=" + getIncentives() +
            ", tradeInEstimate=" + getTradeInEstimate() +
            ", subscriptionServices=" + getSubscriptionServices() +
            ", protectionPlan=" + getProtectionPlan() +
            "}";
    }
}
