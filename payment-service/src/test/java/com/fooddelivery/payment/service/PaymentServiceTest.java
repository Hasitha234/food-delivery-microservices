package com.fooddelivery.payment.service;

import com.fooddelivery.payment.domain.PaymentMethod;
import com.fooddelivery.payment.domain.PaymentStatus;
import com.fooddelivery.payment.dto.PaymentRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PaymentServiceTest {

    private final PaymentService paymentService = new PaymentService();

    @Test
    void shouldCreateSuccessfulPayment() {
        PaymentRequest request = new PaymentRequest();
        request.setOrderId("ORDER-1001");
        request.setUserId("USER-2001");
        request.setAmount(new BigDecimal("2500.00"));
        request.setCurrency("lkr");
        request.setPaymentMethod(PaymentMethod.CARD);

        var payment = paymentService.makePayment(request);

        assertNotNull(payment.getPaymentId());
        assertEquals(PaymentStatus.SUCCESS, payment.getStatus());
        assertEquals("LKR", payment.getCurrency());
        assertNotNull(payment.getTransactionReference());
    }
}
