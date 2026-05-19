package com.fake.tinkoff.service;


import com.fake.tinkoff.model.Payment;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentService {
    private final Map<String, Payment> payments = new HashMap<>();

    public Payment init(String terminalKey, String orderId, long amount) {
        String paymentId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Payment payment = new Payment(paymentId, orderId, terminalKey, amount);
        payments.put(paymentId, payment);
        return payment;
    }

    public Payment getByPaymentId(String paymentId){
        return payments.get(paymentId);
    }

    public boolean confirm(String paymentId){
        Payment payment = payments.get(paymentId);
        if(payment == null) return false;
        payment.setStatus("CONFIRMED");
        return true;
    }

    public boolean finishAuthorize(String paymentId) {
        Payment payment = payments.get(paymentId);
        if (payment == null) return false;
        if (!payment.getStatus().equals("NEW")) return false;
        payment.setStatus("AUTHORIZED");
        return true;
    }

    public boolean cancel(String paymentId){
        Payment payment = payments.get(paymentId);
        if(payment == null) return false;
        payment.setStatus("CANCELED");
        return true;
    }
}
