package com.fooddelivery.delivery.repository;

import com.fooddelivery.delivery.domain.Delivery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class DeliveryRepository {

    // In-memory storage of deliveries
    private final ConcurrentMap<String, Delivery> deliveries = new ConcurrentHashMap<>();

    public Delivery save(Delivery delivery) {
        deliveries.put(delivery.getDeliveryId(), delivery);
        return delivery;
    }

    public Optional<Delivery> findById(String deliveryId) {
        return Optional.ofNullable(deliveries.get(deliveryId));
    }

    public List<Delivery> findAll() {
        return List.copyOf(deliveries.values());
    }

    public List<Delivery> findByOrderId(String orderId) {
        return deliveries.values().stream()
                .filter(d -> d.getOrderId().equals(orderId))
                .toList();
    }

    public boolean existsById(String deliveryId) {
        return deliveries.containsKey(deliveryId);
    }

    public void deleteById(String deliveryId) {
        deliveries.remove(deliveryId);
    }
}
