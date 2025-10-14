package com.ellakim.risk_score_service.model;

public record RiskScoreResponse(
        String customerId,
        int riskScore, // Score from 0 (low risk) to 100 (high risk)
        String riskLevel // e.g., "LOW", "MEDIUM", "HIGH"
) {
}
