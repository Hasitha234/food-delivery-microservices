package com.fooddelivery.delivery.dto;

import com.fooddelivery.delivery.domain.DeliveryStatus;

public class DeliveryStatusResponse {

    private String deliveryId;
    private String orderId;
    private String deliveryAgentId;
    private DeliveryStatus status;

    // Constructors
    public DeliveryStatusResponse() {
    }

    // Getters and Setters
    public String getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDeliveryAgentId() {
        return deliveryAgentId;
    }

    public void setDeliveryAgentId(String deliveryAgentId) {
        this.deliveryAgentId = deliveryAgentId;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }
}
