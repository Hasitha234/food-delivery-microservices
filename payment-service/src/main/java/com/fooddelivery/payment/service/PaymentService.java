package com.fooddelivery.payment.service;

import com.fooddelivery.payment.domain.Payment;
import com.fooddelivery.payment.domain.PaymentStatus;
import com.fooddelivery.payment.dto.PaymentRequest;
import com.fooddelivery.payment.exception.PaymentNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class PaymentService {

    private final ConcurrentMap<String, Payment> payments = new ConcurrentHashMap<>();

    public Payment makePayment(PaymentRequest request) {
        LocalDateTime now = LocalDateTime.now();

        Payment payment = new Payment();
        payment.setPaymentId(UUID.randomUUID().toString());
        payment.setOrderId(request.getOrderId());
        payment.setUserId(request.getUserId());
        payment.setAmount(request.getAmount());
        payment.setCurrency(request.getCurrency().toUpperCase());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setTransactionReference("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        payment.setCreatedAt(now);
        payment.setUpdatedAt(now);

        payments.put(payment.getPaymentId(), payment);
        return payment;
    }

    public Payment getPaymentById(String paymentId) {
        Payment payment = payments.get(paymentId);
        if (payment == null) {
            throw new PaymentNotFoundException(paymentId);
        }
        return payment;
    }

    public PaymentStatus getPaymentStatus(String paymentId) {
        return getPaymentById(paymentId).getStatus();
    }

    public List<Payment> getAllPayments() {
        return new ArrayList<>(payments.values());
    }
}
