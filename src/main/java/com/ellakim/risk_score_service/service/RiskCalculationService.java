package com.ellakim.risk_score_service.service;

import com.ellakim.risk_score_service.model.CustomerActivityMetrics;
import com.ellakim.risk_score_service.model.RiskScoreResponse;
import org.springframework.stereotype.Service;

@Service // Marks this class as a Spring Service component
public class RiskCalculationService {

    public RiskScoreResponse calculateRisk(CustomerActivityMetrics metrics) {
        int score = 0; // Start with a base score of 0 (low risk)

        // --- 1. Login Activity (Higher days since login -> Higher risk) ---
        if (metrics.daysSinceLastLogin() > 30) {
            score += 20; // 20 points for low activity
        } else if (metrics.daysSinceLastLogin() > 7) {
            score += 5;
        }

        // --- 2. Feature Usage (Lower usage -> Higher risk) ---
        if (metrics.averageFeatureUsagePerWeek() < 1) {
            score += 30; // 30 points for almost no usage
        } else if (metrics.averageFeatureUsagePerWeek() < 5) {
            score += 10;
        }

        // --- 3. Payment History (Higher failures -> Higher risk) ---
        score += metrics.paymentFailuresInLastYear() * 15; // 15 points per failure

        // --- 4. Onboarding Status (Incomplete -> Higher risk) ---
        if (!metrics.hasCompletedOnboarding()) {
            score += 10; // 10 points for incomplete setup
        }

        // Cap the score at 100
        int finalScore = Math.min(score, 100);

        String riskLevel = determineRiskLevel(finalScore);

        return new RiskScoreResponse(
                metrics.customerId(),
                finalScore,
                riskLevel
        );
    }

    private String determineRiskLevel(int score) {
        if (score >= 60) {
            return "HIGH";
        } else if (score >= 30) {
            return "MEDIUM";
        } else {
            return "LOW";
        }
    }
}
