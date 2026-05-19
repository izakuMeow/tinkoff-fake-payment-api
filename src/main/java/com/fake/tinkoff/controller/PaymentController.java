package com.fake.tinkoff.controller;

import com.fake.tinkoff.model.Payment;
import com.fake.tinkoff.service.PaymentService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/v2")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // 1. Инициировать платёж
    @PostMapping("/Init")
    public Map<String, Object> init(@RequestBody Map<String, Object> req) {
        String terminalKey = (String) req.get("TerminalKey");
        String orderId     = (String) req.get("OrderId");
        long amount        = Long.parseLong(req.get("Amount").toString());

        Payment payment = paymentService.init(terminalKey, orderId, amount);

        return Map.of(
                "Success",    true,
                "ErrorCode",  "0",
                "PaymentId",  payment.getPaymentId(),
                "Status",     payment.getStatus(),
                "Amount",     payment.getAmount()
        );
    }

    // 2. Подтвердить списание
    @PostMapping("/Confirm")
    public Map<String, Object> confirm(@RequestBody Map<String, Object> req) {
        String paymentId = (String) req.get("PaymentId");
        boolean ok = paymentService.confirm(paymentId);
        return Map.of("Success", ok, "ErrorCode", ok ? "0" : "1002");
    }

    // 3. Отменить платёж
    @PostMapping("/Cancel")
    public Map<String, Object> cancel(@RequestBody Map<String, Object> req) {
        String paymentId = (String) req.get("PaymentId");
        boolean ok = paymentService.cancel(paymentId);
        return Map.of("Success", ok, "ErrorCode", ok ? "0" : "1002");
    }

    // 4. Статус платежа
    @PostMapping("/GetState")
    public Map<String, Object> getState(@RequestBody Map<String, Object> req) {
        String paymentId = (String) req.get("PaymentId");
        Payment payment  = paymentService.getByPaymentId(paymentId);
        if (payment == null) {
            return Map.of("Success", false, "ErrorCode", "1002");
        }
        return Map.of(
                "Success",   true,
                "ErrorCode", "0",
                "PaymentId", payment.getPaymentId(),
                "Status",    payment.getStatus(),
                "Amount",    payment.getAmount()
        );
    }

    // 5. Подтвердить платёж (покупатель ввёл карту)
    @PostMapping("/FinishAuthorize")
    public Map<String, Object> finishAuthorize(@RequestBody Map<String, Object> req) {
        String paymentId = (String) req.get("PaymentId");
        boolean ok = paymentService.finishAuthorize(paymentId);
        return Map.of("Success", ok, "ErrorCode", ok ? "0" : "1002");
    }

}
