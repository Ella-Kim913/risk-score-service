package com.ellakim.risk_score_service.model;

public record CustomerActivityMetrics(
        String customerId,
        int daysSinceLastLogin,
        int averageFeatureUsagePerWeek,
        int paymentFailuresInLastYear,
        boolean hasCompletedOnboarding
) {
    // A Java Record automatically generates the constructor, getters, equals(), and hashCode()
}
