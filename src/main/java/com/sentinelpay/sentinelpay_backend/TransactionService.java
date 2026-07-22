package com.sentinelpay.sentinelpay_backend;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class TransactionService {

    private final List<LocalDateTime> recentTransactions = new CopyOnWriteArrayList<>();

    public TransactionResponse evaluate(TransactionRequest request) {
        int score = 0;
        List<String> reasons = new ArrayList<>();

        if (request.getAmount() > 10000) {
            score += 30;
            reasons.add("High amount transaction (+30)");
        }

        int hour = LocalDateTime.now().getHour();
        if (hour < 5 || hour >= 23) {
            score += 15;
            reasons.add("Unusual transaction hour (+15)");
        }

        LocalDateTime now = LocalDateTime.now();
        recentTransactions.add(now);
        recentTransactions.removeIf(t -> t.isBefore(now.minusSeconds(60)));
        if (recentTransactions.size() > 3) {
            score += 25;
            reasons.add("Multiple transactions in short time (+25)");
        }

        if (reasons.isEmpty()) {
            reasons.add("No risk factors detected");
        }

        String decision;
        if (score >= 70) decision = "BLOCKED";
        else if (score >= 40) decision = "FLAGGED";
        else decision = "APPROVED";

        return new TransactionResponse(request.getAmount(), request.getPayee(), score, decision, reasons);
    }
}
