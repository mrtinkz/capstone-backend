package com.capstone.ecommplatform.web.rest;

import static com.capstone.ecommplatform.domain.PaymentDetailAsserts.*;
import static com.capstone.ecommplatform.web.rest.TestUtil.createUpdateProxyForBean;
import static com.capstone.ecommplatform.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.capstone.ecommplatform.IntegrationTest;
import com.capstone.ecommplatform.domain.PaymentDetail;
import com.capstone.ecommplatform.domain.enumeration.CardType;
import com.capstone.ecommplatform.domain.enumeration.PaymentType;
import com.capstone.ecommplatform.repository.PaymentDetailRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PaymentDetailResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaymentDetailResourceIT {

    private static final Long DEFAULT_ORDER_ID = 1L;
    private static final Long UPDATED_ORDER_ID = 2L;

    private static final PaymentType DEFAULT_PAYMENT_TYPE = PaymentType.CREDIT_CARD;
    private static final PaymentType UPDATED_PAYMENT_TYPE = PaymentType.DEBIT_CARD;

    private static final BigDecimal DEFAULT_PAYMENT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PAYMENT_AMOUNT = new BigDecimal(2);

    private static final LocalDate DEFAULT_PAYMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PAYMENT_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final CardType DEFAULT_CARD_TYPE = CardType.VISA;
    private static final CardType UPDATED_CARD_TYPE = CardType.MASTERCARD;

    private static final String DEFAULT_CARD_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CARD_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_CARD_HOLDER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CARD_HOLDER_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CARD_EXPIRATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CARD_EXPIRATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/payment-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PaymentDetailRepository paymentDetailRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentDetailMockMvc;

    private PaymentDetail paymentDetail;

    private PaymentDetail insertedPaymentDetail;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentDetail createEntity(EntityManager em) {
        PaymentDetail paymentDetail = new PaymentDetail()
            .orderId(DEFAULT_ORDER_ID)
            .paymentType(DEFAULT_PAYMENT_TYPE)
            .paymentAmount(DEFAULT_PAYMENT_AMOUNT)
            .paymentDate(DEFAULT_PAYMENT_DATE)
            .cardType(DEFAULT_CARD_TYPE)
            .cardNumber(DEFAULT_CARD_NUMBER)
            .cardHolderName(DEFAULT_CARD_HOLDER_NAME)
            .cardExpirationDate(DEFAULT_CARD_EXPIRATION_DATE);
        return paymentDetail;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentDetail createUpdatedEntity(EntityManager em) {
        PaymentDetail paymentDetail = new PaymentDetail()
            .orderId(UPDATED_ORDER_ID)
            .paymentType(UPDATED_PAYMENT_TYPE)
            .paymentAmount(UPDATED_PAYMENT_AMOUNT)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .cardType(UPDATED_CARD_TYPE)
            .cardNumber(UPDATED_CARD_NUMBER)
            .cardHolderName(UPDATED_CARD_HOLDER_NAME)
            .cardExpirationDate(UPDATED_CARD_EXPIRATION_DATE);
        return paymentDetail;
    }

    @BeforeEach
    public void initTest() {
        paymentDetail = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedPaymentDetail != null) {
            paymentDetailRepository.delete(insertedPaymentDetail);
            insertedPaymentDetail = null;
        }
    }

    @Test
    @Transactional
    void createPaymentDetail() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PaymentDetail
        var returnedPaymentDetail = om.readValue(
            restPaymentDetailMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDetail)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PaymentDetail.class
        );

        // Validate the PaymentDetail in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPaymentDetailUpdatableFieldsEquals(returnedPaymentDetail, getPersistedPaymentDetail(returnedPaymentDetail));

        insertedPaymentDetail = returnedPaymentDetail;
    }

    @Test
    @Transactional
    void createPaymentDetailWithExistingId() throws Exception {
        // Create the PaymentDetail with an existing ID
        paymentDetail.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentDetailMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDetail)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPaymentTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentDetail.setPaymentType(null);

        // Create the PaymentDetail, which fails.

        restPaymentDetailMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDetail)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaymentAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentDetail.setPaymentAmount(null);

        // Create the PaymentDetail, which fails.

        restPaymentDetailMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDetail)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaymentDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        paymentDetail.setPaymentDate(null);

        // Create the PaymentDetail, which fails.

        restPaymentDetailMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDetail)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPaymentDetails() throws Exception {
        // Initialize the database
        insertedPaymentDetail = paymentDetailRepository.saveAndFlush(paymentDetail);

        // Get all the paymentDetailList
        restPaymentDetailMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentDetail.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderId").value(hasItem(DEFAULT_ORDER_ID.intValue())))
            .andExpect(jsonPath("$.[*].paymentType").value(hasItem(DEFAULT_PAYMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].paymentAmount").value(hasItem(sameNumber(DEFAULT_PAYMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].cardType").value(hasItem(DEFAULT_CARD_TYPE.toString())))
            .andExpect(jsonPath("$.[*].cardNumber").value(hasItem(DEFAULT_CARD_NUMBER)))
            .andExpect(jsonPath("$.[*].cardHolderName").value(hasItem(DEFAULT_CARD_HOLDER_NAME)))
            .andExpect(jsonPath("$.[*].cardExpirationDate").value(hasItem(DEFAULT_CARD_EXPIRATION_DATE.toString())));
    }

    @Test
    @Transactional
    void getPaymentDetail() throws Exception {
        // Initialize the database
        insertedPaymentDetail = paymentDetailRepository.saveAndFlush(paymentDetail);

        // Get the paymentDetail
        restPaymentDetailMockMvc
            .perform(get(ENTITY_API_URL_ID, paymentDetail.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentDetail.getId().intValue()))
            .andExpect(jsonPath("$.orderId").value(DEFAULT_ORDER_ID.intValue()))
            .andExpect(jsonPath("$.paymentType").value(DEFAULT_PAYMENT_TYPE.toString()))
            .andExpect(jsonPath("$.paymentAmount").value(sameNumber(DEFAULT_PAYMENT_AMOUNT)))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.cardType").value(DEFAULT_CARD_TYPE.toString()))
            .andExpect(jsonPath("$.cardNumber").value(DEFAULT_CARD_NUMBER))
            .andExpect(jsonPath("$.cardHolderName").value(DEFAULT_CARD_HOLDER_NAME))
            .andExpect(jsonPath("$.cardExpirationDate").value(DEFAULT_CARD_EXPIRATION_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPaymentDetail() throws Exception {
        // Get the paymentDetail
        restPaymentDetailMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaymentDetail() throws Exception {
        // Initialize the database
        insertedPaymentDetail = paymentDetailRepository.saveAndFlush(paymentDetail);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentDetail
        PaymentDetail updatedPaymentDetail = paymentDetailRepository.findById(paymentDetail.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPaymentDetail are not directly saved in db
        em.detach(updatedPaymentDetail);
        updatedPaymentDetail
            .orderId(UPDATED_ORDER_ID)
            .paymentType(UPDATED_PAYMENT_TYPE)
            .paymentAmount(UPDATED_PAYMENT_AMOUNT)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .cardType(UPDATED_CARD_TYPE)
            .cardNumber(UPDATED_CARD_NUMBER)
            .cardHolderName(UPDATED_CARD_HOLDER_NAME)
            .cardExpirationDate(UPDATED_CARD_EXPIRATION_DATE);

        restPaymentDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPaymentDetail.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPaymentDetail))
            )
            .andExpect(status().isOk());

        // Validate the PaymentDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPaymentDetailToMatchAllProperties(updatedPaymentDetail);
    }

    @Test
    @Transactional
    void putNonExistingPaymentDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentDetail.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentDetail.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentDetail))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaymentDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentDetail.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentDetail))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaymentDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentDetail.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentDetailMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDetail)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentDetailWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentDetail = paymentDetailRepository.saveAndFlush(paymentDetail);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentDetail using partial update
        PaymentDetail partialUpdatedPaymentDetail = new PaymentDetail();
        partialUpdatedPaymentDetail.setId(paymentDetail.getId());

        partialUpdatedPaymentDetail
            .paymentAmount(UPDATED_PAYMENT_AMOUNT)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .cardType(UPDATED_CARD_TYPE)
            .cardNumber(UPDATED_CARD_NUMBER)
            .cardExpirationDate(UPDATED_CARD_EXPIRATION_DATE);

        restPaymentDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentDetail.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentDetail))
            )
            .andExpect(status().isOk());

        // Validate the PaymentDetail in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentDetailUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPaymentDetail, paymentDetail),
            getPersistedPaymentDetail(paymentDetail)
        );
    }

    @Test
    @Transactional
    void fullUpdatePaymentDetailWithPatch() throws Exception {
        // Initialize the database
        insertedPaymentDetail = paymentDetailRepository.saveAndFlush(paymentDetail);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paymentDetail using partial update
        PaymentDetail partialUpdatedPaymentDetail = new PaymentDetail();
        partialUpdatedPaymentDetail.setId(paymentDetail.getId());

        partialUpdatedPaymentDetail
            .orderId(UPDATED_ORDER_ID)
            .paymentType(UPDATED_PAYMENT_TYPE)
            .paymentAmount(UPDATED_PAYMENT_AMOUNT)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .cardType(UPDATED_CARD_TYPE)
            .cardNumber(UPDATED_CARD_NUMBER)
            .cardHolderName(UPDATED_CARD_HOLDER_NAME)
            .cardExpirationDate(UPDATED_CARD_EXPIRATION_DATE);

        restPaymentDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaymentDetail.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaymentDetail))
            )
            .andExpect(status().isOk());

        // Validate the PaymentDetail in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentDetailUpdatableFieldsEquals(partialUpdatedPaymentDetail, getPersistedPaymentDetail(partialUpdatedPaymentDetail));
    }

    @Test
    @Transactional
    void patchNonExistingPaymentDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentDetail.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentDetail.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentDetail))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaymentDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentDetail.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentDetail))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaymentDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaymentDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paymentDetail.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentDetailMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(paymentDetail)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaymentDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaymentDetail() throws Exception {
        // Initialize the database
        insertedPaymentDetail = paymentDetailRepository.saveAndFlush(paymentDetail);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the paymentDetail
        restPaymentDetailMockMvc
            .perform(delete(ENTITY_API_URL_ID, paymentDetail.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return paymentDetailRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected PaymentDetail getPersistedPaymentDetail(PaymentDetail paymentDetail) {
        return paymentDetailRepository.findById(paymentDetail.getId()).orElseThrow();
    }

    protected void assertPersistedPaymentDetailToMatchAllProperties(PaymentDetail expectedPaymentDetail) {
        assertPaymentDetailAllPropertiesEquals(expectedPaymentDetail, getPersistedPaymentDetail(expectedPaymentDetail));
    }

    protected void assertPersistedPaymentDetailToMatchUpdatableProperties(PaymentDetail expectedPaymentDetail) {
        assertPaymentDetailAllUpdatablePropertiesEquals(expectedPaymentDetail, getPersistedPaymentDetail(expectedPaymentDetail));
    }
}
