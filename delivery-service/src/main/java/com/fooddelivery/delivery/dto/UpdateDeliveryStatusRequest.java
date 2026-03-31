package com.fooddelivery.delivery.dto;

import com.fooddelivery.delivery.domain.DeliveryStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateDeliveryStatusRequest {

    @NotNull(message = "Status is required")
    private DeliveryStatus status;

    // Constructors
    public UpdateDeliveryStatusRequest() {
    }

    public UpdateDeliveryStatusRequest(DeliveryStatus status) {
        this.status = status;
    }

    // Getters and Setters
    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }
}
