package com.fooddelivery.delivery.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/")
@Tag(name = "Root", description = "Root API information and service status")
public class RootController {

    @GetMapping
    @Operation(summary = "Service Information", description = "Get delivery service information and available endpoints")
    public Map<String, Object> serviceInfo() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("service", "Delivery Service");
        response.put("version", "1.0.0");
        response.put("status", "UP");
        response.put("endpoints", Map.of(
            "api", "/api/deliveries",
            "swagger", "/swagger-ui.html",
            "api-docs", "/v3/api-docs"
        ));
        response.put("description", "Food Delivery Microservice - Handles order deliveries, tracking, and delivery agent assignments");
        return response;
    }
}
