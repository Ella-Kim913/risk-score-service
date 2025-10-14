package com.ellakim.risk_score_service.service;

// src/test/java/com/example/riskscore/service/RiskCalculationServiceTest.java

import com.ellakim.risk_score_service.model.CustomerActivityMetrics;
import  com.ellakim.risk_score_service.model.RiskScoreResponse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

// We instantiate the service directly, treating it as a standard Java class
public class RiskCalculationServiceTest {

    private final RiskCalculationService service = new RiskCalculationService();

    @Test
    void highRiskMetricsShouldResultInHighScore() {
        // ARRANGE: Setup the input data for a clear high-risk scenario
        CustomerActivityMetrics highRiskMetrics = new CustomerActivityMetrics(
                "user-HIGH-001",
                40,   // daysSinceLastLogin
                0,    // averageFeatureUsagePerWeek
                2,    // paymentFailuresInLastYear (2 * 15 = 30 points)
                false // hasCompletedOnboarding
        );

        // ACT: Call the method under test
        RiskScoreResponse response = service.calculateRisk(highRiskMetrics);

        // ASSERT: Verify the expected output
        // Expected Score: 20 (login) + 30 (usage) + 30 (failures) + 10 (onboarding) = 90
        assertEquals(90, response.riskScore(), "The calculated risk score should be 90.");
        assertEquals("HIGH", response.riskLevel(), "The risk level should be HIGH.");
    }

    @Test
    void lowRiskMetricsShouldResultInLowScore() {
        // ARRANGE: Setup the input data for a clear low-risk scenario
        CustomerActivityMetrics lowRiskMetrics = new CustomerActivityMetrics(
                "user-LOW-001",
                1,   // daysSinceLastLogin
                15,  // averageFeatureUsagePerWeek
                0,   // paymentFailuresInLastYear
                true // hasCompletedOnboarding
        );

        // ACT
        RiskScoreResponse response = service.calculateRisk(lowRiskMetrics);

        // ASSERT
        assertEquals(0, response.riskScore(), "The calculated risk score should be 0.");
        assertEquals("LOW", response.riskLevel(), "The risk level should be LOW.");
    }
}
