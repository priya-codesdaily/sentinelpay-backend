package com.sentinelpay.sentinelpay_backend;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;
    private String payee;
    private int riskScore;
    private String decision;
    private String deviceFingerprint;
    private LocalDateTime createdAt;

    public TransactionEntity() {}

    public TransactionEntity(double amount, String payee, int riskScore, String decision, String deviceFingerprint) {
        this.amount = amount;
        this.payee = payee;
        this.riskScore = riskScore;
        this.decision = decision;
        this.deviceFingerprint = deviceFingerprint;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public double getAmount() { return amount; }
    public String getPayee() { return payee; }
    public int getRiskScore() { return riskScore; }
    public String getDecision() { return decision; }
    public String getDeviceFingerprint() { return deviceFingerprint; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}