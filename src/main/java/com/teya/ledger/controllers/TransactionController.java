package com.teya.ledger.controllers;

import com.teya.ledger.models.TransactionEntry;
import com.teya.ledger.models.TransactionRequest;
import com.teya.ledger.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionEntry transact(@RequestBody @Valid TransactionRequest transaction) {
        final TransactionEntry transactionEntry
                = new TransactionEntry(transaction.amount(), transaction.transactionType(), transaction.description());
        return transactionService.addTransaction(transactionEntry);
    }
}
