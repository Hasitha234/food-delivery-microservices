package com.fooddelivery.delivery.service;

import com.fooddelivery.delivery.domain.Delivery;
import com.fooddelivery.delivery.domain.DeliveryStatus;
import com.fooddelivery.delivery.dto.DeliveryRequest;
import com.fooddelivery.delivery.exception.DeliveryNotFoundException;
import com.fooddelivery.delivery.repository.DeliveryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    public DeliveryService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    /**
     * Create a new delivery for an order
     */
    public Delivery createDelivery(DeliveryRequest request) {
        LocalDateTime now = LocalDateTime.now();

        Delivery delivery = new Delivery();
        delivery.setDeliveryId(UUID.randomUUID().toString());
        delivery.setOrderId(request.getOrderId());
        delivery.setRestaurantId(request.getRestaurantId());
        delivery.setCustomerId(request.getCustomerId());
        delivery.setPickupAddress(request.getPickupAddress());
        delivery.setDeliveryAddress(request.getDeliveryAddress());
        delivery.setNotes(request.getNotes());
        delivery.setStatus(DeliveryStatus.PENDING);
        delivery.setCreatedAt(now);
        delivery.setUpdatedAt(now);

        return deliveryRepository.save(delivery);
    }

    /**
     * Get a delivery by its ID
     */
    public Delivery getDeliveryById(String deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DeliveryNotFoundException(deliveryId));
    }

    /**
     * Get all deliveries
     */
    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }

    /**
     * Get deliveries for a specific order
     */
    public List<Delivery> getDeliveriesByOrderId(String orderId) {
        return deliveryRepository.findByOrderId(orderId);
    }

    /**
     * Update delivery status (e.g., PENDING to OUT_FOR_DELIVERY or DELIVERED)
     */
    public Delivery updateDeliveryStatus(String deliveryId, DeliveryStatus newStatus) {
        Delivery delivery = getDeliveryById(deliveryId);
        delivery.setStatus(newStatus);
        delivery.setUpdatedAt(LocalDateTime.now());

        if (newStatus == DeliveryStatus.DELIVERED) {
            delivery.setDeliveredAt(LocalDateTime.now());
        }

        return deliveryRepository.save(delivery);
    }

    /**
     * Assign a delivery agent to a delivery
     */
    public Delivery assignDeliveryAgent(String deliveryId, String agentId) {
        Delivery delivery = getDeliveryById(deliveryId);
        delivery.setDeliveryAgentId(agentId);
        delivery.setUpdatedAt(LocalDateTime.now());
        return deliveryRepository.save(delivery);
    }

    /**
     * Get the current status of a delivery
     */
    public DeliveryStatus getDeliveryStatus(String deliveryId) {
        return getDeliveryById(deliveryId).getStatus();
    }
}
