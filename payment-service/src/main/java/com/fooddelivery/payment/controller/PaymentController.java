package com.fooddelivery.payment.controller;

import com.fooddelivery.payment.domain.Payment;
import com.fooddelivery.payment.dto.PaymentRequest;
import com.fooddelivery.payment.dto.PaymentResponse;
import com.fooddelivery.payment.dto.PaymentStatusResponse;
import com.fooddelivery.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payment Service", description = "Endpoints for processing and checking food order payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Make payment", description = "Create a new payment for an order")
    public PaymentResponse makePayment(@Valid @RequestBody PaymentRequest request) {
        return toResponse(paymentService.makePayment(request));
    }

    @GetMapping("/{paymentId}")
    @Operation(summary = "View payment details", description = "Get complete payment details by payment ID")
    public PaymentResponse getPayment(@PathVariable String paymentId) {
        return toResponse(paymentService.getPaymentById(paymentId));
    }

    @GetMapping("/status/{paymentId}")
    @Operation(summary = "Check payment status", description = "Get only the payment status information")
    public PaymentStatusResponse getPaymentStatus(@PathVariable String paymentId) {
        Payment payment = paymentService.getPaymentById(paymentId);
        PaymentStatusResponse response = new PaymentStatusResponse();
        response.setPaymentId(payment.getPaymentId());
        response.setOrderId(payment.getOrderId());
        response.setStatus(payment.getStatus());
        response.setTransactionReference(payment.getTransactionReference());
        return response;
    }

    @GetMapping
    @Operation(summary = "View all payments", description = "Helper endpoint for demo and testing")
    public List<PaymentResponse> getAllPayments() {
        return paymentService.getAllPayments().stream()
                .map(this::toResponse)
                .toList();
    }

    private PaymentResponse toResponse(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setPaymentId(payment.getPaymentId());
        response.setOrderId(payment.getOrderId());
        response.setUserId(payment.getUserId());
        response.setAmount(payment.getAmount());
        response.setCurrency(payment.getCurrency());
        response.setPaymentMethod(payment.getPaymentMethod());
        response.setStatus(payment.getStatus());
        response.setTransactionReference(payment.getTransactionReference());
        response.setCreatedAt(payment.getCreatedAt());
        response.setUpdatedAt(payment.getUpdatedAt());
        return response;
    }
}
