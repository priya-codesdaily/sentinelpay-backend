package com.sentinelpay.sentinelpay_backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ConcurrentHashMap;
@Service
public class TransactionService {

    private final List<LocalDateTime> recentTransactions = new CopyOnWriteArrayList<>();
    @Autowired
    private TransactionRepository transactionRepository;
    private final Map<String, Set<String>> knownDevicesByPayee = new ConcurrentHashMap<>();

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
            int extra = Math.min((recentTransactions.size() - 3) * 20, 80);
            score += extra;
            reasons.add("Multiple transactions in short time (+" + extra + ")");
        }

        if (reasons.isEmpty()) {
            reasons.add("No risk factors detected");
        }
        Set<String> knownDevices = knownDevicesByPayee.computeIfAbsent(request.getPayee(), k -> new HashSet<>());
        if (!knownDevices.isEmpty() && !knownDevices.contains(request.getDeviceFingerprint())) {
            score += 20;
            reasons.add("Transaction from unrecognized device (+20)");
        }
        knownDevices.add(request.getDeviceFingerprint());

        if (reasons.isEmpty()) {
            reasons.add("No risk factors detected");
        }

        String decision;
        if (score >= 70) decision = "BLOCKED";
        else if (score >= 40) decision = "FLAGGED";
        else decision = "APPROVED";

        TransactionEntity entity = new TransactionEntity(
                request.getAmount(),
                request.getPayee(),
                score,
                decision,
                request.getDeviceFingerprint()
        );
        transactionRepository.save(entity);

        return new TransactionResponse(request.getAmount(), request.getPayee(), score, decision, reasons);
    }
}
