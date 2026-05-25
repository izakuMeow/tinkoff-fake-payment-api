package com.fake.tinkoff.service;

import com.fake.tinkoff.model.Payment;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class WebhookService {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public void send(Payment payment) {
        String url = payment.getNotificationUrl();
        if (url == null || url.isBlank()) return;

        String body = String.format(
                "{\"PaymentId\":\"%s\",\"Status\":\"%s\",\"Amount\":%d,\"OrderId\":\"%s\"}",
                payment.getPaymentId(),
                payment.getStatus(),
                payment.getAmount(),
                payment.getOrderId()
        );

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response ->
                            System.out.println("Webhook sent to " + url + " → " + response.statusCode())
                    );
        } catch (Exception e) {
            System.out.println("Webhook error: " + e.getMessage());
        }
    }
}
