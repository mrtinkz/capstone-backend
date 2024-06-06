package com.capstone.ecommplatform.domain;

import com.capstone.ecommplatform.domain.enumeration.CardType;
import com.capstone.ecommplatform.domain.enumeration.PaymentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A PaymentDetail.
 */
@Entity
@Table(name = "payment_detail")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentDetail implements Serializable {

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
    @Column(name = "payment_type", nullable = false)
    private PaymentType paymentType;

    @NotNull
    @Column(name = "payment_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal paymentAmount;

    @NotNull
    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_type")
    private CardType cardType;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "card_holder_name")
    private String cardHolderName;

    @Column(name = "card_expiration_date")
    private LocalDate cardExpirationDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PaymentDetail id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return this.orderId;
    }

    public PaymentDetail orderId(Long orderId) {
        this.setOrderId(orderId);
        return this;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public PaymentType getPaymentType() {
        return this.paymentType;
    }

    public PaymentDetail paymentType(PaymentType paymentType) {
        this.setPaymentType(paymentType);
        return this;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public BigDecimal getPaymentAmount() {
        return this.paymentAmount;
    }

    public PaymentDetail paymentAmount(BigDecimal paymentAmount) {
        this.setPaymentAmount(paymentAmount);
        return this;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public LocalDate getPaymentDate() {
        return this.paymentDate;
    }

    public PaymentDetail paymentDate(LocalDate paymentDate) {
        this.setPaymentDate(paymentDate);
        return this;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public CardType getCardType() {
        return this.cardType;
    }

    public PaymentDetail cardType(CardType cardType) {
        this.setCardType(cardType);
        return this;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public String getCardNumber() {
        return this.cardNumber;
    }

    public PaymentDetail cardNumber(String cardNumber) {
        this.setCardNumber(cardNumber);
        return this;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolderName() {
        return this.cardHolderName;
    }

    public PaymentDetail cardHolderName(String cardHolderName) {
        this.setCardHolderName(cardHolderName);
        return this;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public LocalDate getCardExpirationDate() {
        return this.cardExpirationDate;
    }

    public PaymentDetail cardExpirationDate(LocalDate cardExpirationDate) {
        this.setCardExpirationDate(cardExpirationDate);
        return this;
    }

    public void setCardExpirationDate(LocalDate cardExpirationDate) {
        this.cardExpirationDate = cardExpirationDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentDetail)) {
            return false;
        }
        return getId() != null && getId().equals(((PaymentDetail) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentDetail{" +
            "id=" + getId() +
            ", orderId=" + getOrderId() +
            ", paymentType='" + getPaymentType() + "'" +
            ", paymentAmount=" + getPaymentAmount() +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", cardType='" + getCardType() + "'" +
            ", cardNumber='" + getCardNumber() + "'" +
            ", cardHolderName='" + getCardHolderName() + "'" +
            ", cardExpirationDate='" + getCardExpirationDate() + "'" +
            "}";
    }
}
