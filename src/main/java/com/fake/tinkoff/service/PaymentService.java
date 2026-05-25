package com.fake.tinkoff.service;

import com.fake.tinkoff.model.Payment;
import com.fake.tinkoff.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final WebhookService webhookService;

    public PaymentService(PaymentRepository paymentRepository, WebhookService webhookService) {
        this.paymentRepository = paymentRepository;
        this.webhookService    = webhookService;
    }

    public Payment init(String terminalKey, String orderId, long amount, String notificationUrl) {
        String paymentId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Payment payment = new Payment(paymentId, orderId, terminalKey, amount, notificationUrl);
        return paymentRepository.save(payment);
    }

    public Payment getByPaymentId(String paymentId) {
        return paymentRepository.findById(paymentId).orElse(null);
    }

    public boolean finishAuthorize(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElse(null);
        if (payment == null) return false;
        if (!payment.getStatus().equals("NEW")) return false;
        payment.setStatus("AUTHORIZED");
        paymentRepository.save(payment);
        webhookService.send(payment);
        return true;
    }

    public boolean confirm(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElse(null);
        if (payment == null) return false;
        payment.setStatus("CONFIRMED");
        paymentRepository.save(payment);
        webhookService.send(payment);
        return true;
    }

    public boolean cancel(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElse(null);
        if (payment == null) return false;
        payment.setStatus("CANCELED");
        paymentRepository.save(payment);
        webhookService.send(payment);
        return true;
    }
}