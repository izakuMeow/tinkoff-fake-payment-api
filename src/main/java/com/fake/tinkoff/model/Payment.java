package com.fake.tinkoff.model;

public class Payment {
    private String paymentId;
    private String orderId;
    private String terminalKey;
    private long amount;
    private String status;

    public Payment(String paymentId, String orderId, String terminalKey, long amount) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
        this.terminalKey = terminalKey;
        this.status = "NEW";
    }

    public String getPaymentId() { return  paymentId; }
    public String getOrderId() { return  orderId; }
    public String getTerminalKey() { return terminalKey; }
    public long getAmount() { return amount; }
    public String getStatus() { return status; }

    public void setStatus(String status) {
        this.status = status;
    }
}
