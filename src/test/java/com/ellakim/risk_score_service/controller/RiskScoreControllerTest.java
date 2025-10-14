package com.ellakim.risk_score_service.controller;

import com.ellakim.risk_score_service.service.RiskCalculationService;
import com.ellakim.risk_score_service.model.RiskScoreResponse;
import com.ellakim.risk_score_service.model.CustomerActivityMetrics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(RiskScoreController.class) // Only loads the web layer context
public class RiskScoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RiskCalculationService riskCalculationService;

    @Test
    void shouldReturnHighRiskScoreWhenPostingMetrics() throws Exception {
        RiskScoreResponse mockResponse = new RiskScoreResponse("user-HIGH", 90, "HIGH");
        when(riskCalculationService.calculateRisk(any(CustomerActivityMetrics.class)))
                .thenReturn(mockResponse);

        String inputJson = """
            {
                "customerId": "user-HIGH",
                "daysSinceLastLogin": 50,
                "averageFeatureUsagePerWeek": 0,
                "paymentFailuresInLastYear": 2,
                "hasCompletedOnboarding": false
            }
        """;

        mockMvc.perform(post("/api/v1/risk/calculate")
                        .contentType("application/json")
                        .content(inputJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("user-HIGH"))
                .andExpect(jsonPath("$.riskScore").value(90));
    }

    @Test
    void shouldReturnLowRiskScoreWhenPostingMetrics() throws Exception {
        RiskScoreResponse mockResponse = new RiskScoreResponse("user-LOW", 10, "LOW");
        when(riskCalculationService.calculateRisk(any(CustomerActivityMetrics.class)))
                .thenReturn(mockResponse);

        String inputJson = """
            {
                "customerId": "user-LOW",
                "daysSinceLastLogin": 1,
                "averageFeatureUsagePerWeek": 10,
                "paymentFailuresInLastYear": 0,
                "hasCompletedOnboarding": true
            }
        """;

        mockMvc.perform(post("/api/v1/risk/calculate")
                        .contentType("application/json")
                        .content(inputJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("user-LOW"))
                .andExpect(jsonPath("$.riskScore").value(10));
    }
}