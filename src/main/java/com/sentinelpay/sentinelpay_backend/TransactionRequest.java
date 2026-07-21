package com.sentinelpay.sentinelpay_backend;

public class TransactionRequest {
    private double amount;
    private String payee;

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getPayee() { return payee; }
    public void setPayee(String payee) { this.payee = payee; }
}