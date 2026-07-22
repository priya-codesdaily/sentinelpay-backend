package com.sentinelpay.sentinelpay_backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transaction")
    public TransactionResponse processTransaction(@RequestBody TransactionRequest request) {
        return transactionService.evaluate(request);
    }
}