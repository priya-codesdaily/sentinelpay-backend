package com.sentinelpay.sentinelpay_backend;

import java.util.List;

public class TransactionResponse {
    private double amount;
    private String payee;
    private int riskScore;
    private String decision;
    private List<String> reasons;

    public TransactionResponse(double amount, String payee, int riskScore, String decision, List<String> reasons) {
        this.amount = amount;
        this.payee = payee;
        this.riskScore = riskScore;
        this.decision = decision;
        this.reasons = reasons;
    }

    public double getAmount() { return amount; }
    public String getPayee() { return payee; }
    public int getRiskScore() { return riskScore; }
    public String getDecision() { return decision; }
    public List<String> getReasons() { return reasons; }
}