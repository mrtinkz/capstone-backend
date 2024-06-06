package com.capstone.ecommplatform.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class PostPurchaseActivityAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPostPurchaseActivityAllPropertiesEquals(PostPurchaseActivity expected, PostPurchaseActivity actual) {
        assertPostPurchaseActivityAutoGeneratedPropertiesEquals(expected, actual);
        assertPostPurchaseActivityAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPostPurchaseActivityAllUpdatablePropertiesEquals(PostPurchaseActivity expected, PostPurchaseActivity actual) {
        assertPostPurchaseActivityUpdatableFieldsEquals(expected, actual);
        assertPostPurchaseActivityUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPostPurchaseActivityAutoGeneratedPropertiesEquals(PostPurchaseActivity expected, PostPurchaseActivity actual) {
        assertThat(expected)
            .as("Verify PostPurchaseActivity auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPostPurchaseActivityUpdatableFieldsEquals(PostPurchaseActivity expected, PostPurchaseActivity actual) {
        assertThat(expected)
            .as("Verify PostPurchaseActivity relevant properties")
            .satisfies(e -> assertThat(e.getOrderId()).as("check orderId").isEqualTo(actual.getOrderId()))
            .satisfies(e -> assertThat(e.getActivityType()).as("check activityType").isEqualTo(actual.getActivityType()))
            .satisfies(e -> assertThat(e.getStatus()).as("check status").isEqualTo(actual.getStatus()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPostPurchaseActivityUpdatableRelationshipsEquals(PostPurchaseActivity expected, PostPurchaseActivity actual) {}
}
