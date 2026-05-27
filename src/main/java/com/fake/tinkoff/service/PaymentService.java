package com.fake.tinkoff.service;

import com.fake.tinkoff.model.Payment;
import com.fake.tinkoff.repository.PaymentRepository;
import com.fake.tinkoff.repository.TerminalRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final WebhookService webhookService;
    private final TerminalRepository terminalRepository;

    public PaymentService(PaymentRepository paymentRepository, WebhookService webhookService, TerminalRepository terminalRepository) {
        this.paymentRepository = paymentRepository;
        this.webhookService = webhookService;
        this.terminalRepository = terminalRepository;
    }

    public Payment init(String terminalKey, String orderId, long amount, String notificationUrl) {
        if (!terminalRepository.exists(terminalKey)){
            return null;
        }
        String paymentId = String.valueOf(100000000L + (long)(Math.random() * 900000000L));
        Payment payment = new Payment(paymentId, orderId, terminalKey, amount, notificationUrl);
        paymentRepository.save(payment);
        return payment;
    }

    public Payment getByPaymentId(String paymentId) {
        return paymentRepository.findById(paymentId);
    }

    public boolean finishAuthorize(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId);
        if (payment == null) return false;
        if (!payment.getStatus().equals("NEW")) return false;
        payment.setStatus("AUTHORIZED");
        paymentRepository.save(payment);
        webhookService.send(payment);
        return true;
    }

    public List<Payment> getByOrderId(String orderId) {
        return paymentRepository.findByOrderId(orderId);
    }

    public boolean confirm(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId);
        if (payment == null) return false;
        payment.setStatus("CONFIRMED");
        paymentRepository.save(payment);
        webhookService.send(payment);
        return true;
    }

    public boolean cancel(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId);
        if (payment == null) return false;
        payment.setStatus("CANCELED");
        paymentRepository.save(payment);
        webhookService.send(payment);
        return true;
    }
}