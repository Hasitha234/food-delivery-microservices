package com.fooddelivery.delivery.controller;

import com.fooddelivery.delivery.domain.Delivery;
import com.fooddelivery.delivery.domain.DeliveryStatus;
import com.fooddelivery.delivery.dto.DeliveryRequest;
import com.fooddelivery.delivery.dto.DeliveryResponse;
import com.fooddelivery.delivery.dto.DeliveryStatusResponse;
import com.fooddelivery.delivery.service.DeliveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
@Tag(name = "Delivery Service", description = "Endpoints for managing food order deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    /**
     * Create a new delivery (assign delivery to an order)
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Assign delivery to order", description = "Create a new delivery for an order")
    public DeliveryResponse createDelivery(@Valid @RequestBody DeliveryRequest request) {
        return toResponse(deliveryService.createDelivery(request));
    }

    /**
     * Get a delivery by ID
     */
    @GetMapping("/{deliveryId}")
    @Operation(summary = "View single delivery", description = "Get complete delivery details by delivery ID")
    public DeliveryResponse getDelivery(@PathVariable("deliveryId") String deliveryId) {
        return toResponse(deliveryService.getDeliveryById(deliveryId));
    }

    /**
     * Get all deliveries
     */
    @GetMapping
    @Operation(summary = "View all deliveries", description = "Get list of all deliveries (for demo and testing)")
    public List<DeliveryResponse> getAllDeliveries() {
        return deliveryService.getAllDeliveries().stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Get deliveries for a specific order
     */
    @GetMapping("/by-order/{orderId}")
    @Operation(summary = "View deliveries by order", description = "Get all deliveries for a specific order")
    public List<DeliveryResponse> getDeliveriesByOrder(@PathVariable("orderId") String orderId) {
        return deliveryService.getDeliveriesByOrderId(orderId).stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Get the status of a delivery
     */
    @GetMapping("/{deliveryId}/status")
    @Operation(summary = "Track delivery status", description = "Get current status of a delivery")
    public DeliveryStatusResponse getDeliveryStatus(@PathVariable("deliveryId") String deliveryId) {
        Delivery delivery = deliveryService.getDeliveryById(deliveryId);
        DeliveryStatusResponse response = new DeliveryStatusResponse();
        response.setDeliveryId(delivery.getDeliveryId());
        response.setOrderId(delivery.getOrderId());
        response.setDeliveryAgentId(delivery.getDeliveryAgentId());
        response.setStatus(delivery.getStatus());
        return response;
    }

    /**
     * Update delivery status (e.g., PENDING → OUT_FOR_DELIVERY → DELIVERED)
     */
    @PutMapping("/{deliveryId}/status")
    @Operation(summary = "Update delivery status", description = "Update the status of a delivery (PENDING, OUT_FOR_DELIVERY, DELIVERED, CANCELLED)")
    public DeliveryResponse updateDeliveryStatus(
            @PathVariable("deliveryId") String deliveryId,
            @RequestParam("status") DeliveryStatus newStatus) {
        return toResponse(deliveryService.updateDeliveryStatus(deliveryId, newStatus));
    }

    /**
     * Assign a delivery agent to a delivery
     */
    @PutMapping("/{deliveryId}/assign-agent")
    @Operation(summary = "Assign delivery agent", description = "Assign a delivery agent to handle the delivery")
    public DeliveryResponse assignDeliveryAgent(
            @PathVariable("deliveryId") String deliveryId,
            @RequestParam("agentId") String agentId) {
        return toResponse(deliveryService.assignDeliveryAgent(deliveryId, agentId));
    }

    /**
     * Helper method to convert Delivery domain object to DeliveryResponse DTO
     */
    private DeliveryResponse toResponse(Delivery delivery) {
        DeliveryResponse response = new DeliveryResponse();
        response.setDeliveryId(delivery.getDeliveryId());
        response.setOrderId(delivery.getOrderId());
        response.setRestaurantId(delivery.getRestaurantId());
        response.setCustomerId(delivery.getCustomerId());
        response.setDeliveryAgentId(delivery.getDeliveryAgentId());
        response.setPickupAddress(delivery.getPickupAddress());
        response.setDeliveryAddress(delivery.getDeliveryAddress());
        response.setStatus(delivery.getStatus());
        response.setCreatedAt(delivery.getCreatedAt());
        response.setUpdatedAt(delivery.getUpdatedAt());
        response.setDeliveredAt(delivery.getDeliveredAt());
        response.setNotes(delivery.getNotes());
        return response;
    }
}
