package com.sentinelpay.sentinelpay_backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionRepository transactionRepository;

    @PostMapping("/transaction")
    public TransactionResponse processTransaction(@RequestBody TransactionRequest request) {
        return transactionService.evaluate(request);
    }

    @GetMapping("/transactions")
    public List<TransactionEntity> getAllTransactions() {
        return transactionRepository.findAll();
    }
}