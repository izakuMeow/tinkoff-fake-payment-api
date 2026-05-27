package com.fake.tinkoff.model;

public class Payment {

    private String paymentId;
    private String orderId;
    private String terminalKey;
    private long amount;
    private String status;
    private String notificationUrl;

    public Payment(String paymentId, String orderId, String terminalKey, long amount, String notificationUrl) {
        this.paymentId       = paymentId;
        this.orderId         = orderId;
        this.terminalKey     = terminalKey;
        this.amount          = amount;
        this.status          = "NEW";
        this.notificationUrl = notificationUrl;
    }

    public String getPaymentId()        { return paymentId; }
    public String getOrderId()          { return orderId; }
    public String getTerminalKey()      { return terminalKey; }
    public long   getAmount()           { return amount; }
    public String getStatus()           { return status; }
    public String getNotificationUrl()  { return notificationUrl; }

    public void setStatus(String status) { this.status = status; }
}