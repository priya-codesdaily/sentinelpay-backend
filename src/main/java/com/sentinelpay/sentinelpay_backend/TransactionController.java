package com.sentinelpay.sentinelpay_backend;

import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class TransactionController {

    @PostMapping("/transaction")
    public String processTransaction(@RequestBody TransactionRequest request) {
        return "Received: " + request.getAmount() + " to " + request.getPayee();
    }
}