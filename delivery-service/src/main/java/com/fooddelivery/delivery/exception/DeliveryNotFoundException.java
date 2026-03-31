package com.fooddelivery.delivery.exception;

public class DeliveryNotFoundException extends RuntimeException {

    private final String deliveryId;

    public DeliveryNotFoundException(String deliveryId) {
        super("Delivery not found with ID: " + deliveryId);
        this.deliveryId = deliveryId;
    }

    public String getDeliveryId() {
        return deliveryId;
    }
}
