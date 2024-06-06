package com.capstone.ecommplatform.domain;

import static com.capstone.ecommplatform.domain.AssertUtils.bigDecimalCompareTo;
import static org.assertj.core.api.Assertions.assertThat;

public class PricingSummaryAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPricingSummaryAllPropertiesEquals(PricingSummary expected, PricingSummary actual) {
        assertPricingSummaryAutoGeneratedPropertiesEquals(expected, actual);
        assertPricingSummaryAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPricingSummaryAllUpdatablePropertiesEquals(PricingSummary expected, PricingSummary actual) {
        assertPricingSummaryUpdatableFieldsEquals(expected, actual);
        assertPricingSummaryUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPricingSummaryAutoGeneratedPropertiesEquals(PricingSummary expected, PricingSummary actual) {
        assertThat(expected)
            .as("Verify PricingSummary auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPricingSummaryUpdatableFieldsEquals(PricingSummary expected, PricingSummary actual) {
        assertThat(expected)
            .as("Verify PricingSummary relevant properties")
            .satisfies(e -> assertThat(e.getMsrp()).as("check msrp").usingComparator(bigDecimalCompareTo).isEqualTo(actual.getMsrp()))
            .satisfies(
                e ->
                    assertThat(e.getTaxesAndFees())
                        .as("check taxesAndFees")
                        .usingComparator(bigDecimalCompareTo)
                        .isEqualTo(actual.getTaxesAndFees())
            )
            .satisfies(
                e ->
                    assertThat(e.getIncentives())
                        .as("check incentives")
                        .usingComparator(bigDecimalCompareTo)
                        .isEqualTo(actual.getIncentives())
            )
            .satisfies(
                e ->
                    assertThat(e.getTradeInEstimate())
                        .as("check tradeInEstimate")
                        .usingComparator(bigDecimalCompareTo)
                        .isEqualTo(actual.getTradeInEstimate())
            )
            .satisfies(
                e ->
                    assertThat(e.getSubscriptionServices())
                        .as("check subscriptionServices")
                        .usingComparator(bigDecimalCompareTo)
                        .isEqualTo(actual.getSubscriptionServices())
            )
            .satisfies(
                e ->
                    assertThat(e.getProtectionPlan())
                        .as("check protectionPlan")
                        .usingComparator(bigDecimalCompareTo)
                        .isEqualTo(actual.getProtectionPlan())
            );
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPricingSummaryUpdatableRelationshipsEquals(PricingSummary expected, PricingSummary actual) {}
}
