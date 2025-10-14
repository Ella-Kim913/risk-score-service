package com.ellakim.risk_score_service.controller;

import com.ellakim.risk_score_service.service.RiskCalculationService;
import com.ellakim.risk_score_service.model.RiskScoreResponse;
import com.ellakim.risk_score_service.model.CustomerActivityMetrics;
import org.springframework.web.bind.annotation.*;

@RestController // Marks this as a REST controller
@RequestMapping("/api/v1/risk") // Base path for all endpoints in this controller
public class RiskScoreController {

    private final RiskCalculationService riskCalculationService;

    // Constructor Injection: Spring automatically provides an instance of RiskCalculationService
    public RiskScoreController(RiskCalculationService riskCalculationService) {
        this.riskCalculationService = riskCalculationService;
    }

    @PostMapping("/calculate") // Maps POST requests to /api/v1/risk/calculate
    public RiskScoreResponse calculateRisk(@RequestBody CustomerActivityMetrics metrics) {
        // Log the incoming request (optional, but good practice)
        System.out.println("Received request for customer: " + metrics.customerId());

        return riskCalculationService.calculateRisk(metrics);
    }

    @GetMapping("/healthz")
    public String healthCheck() {
        // This is the fastest check, only ensuring the Controller is running.
        return "OK";
    }
}
