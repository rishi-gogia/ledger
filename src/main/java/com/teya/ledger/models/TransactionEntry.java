package com.teya.ledger.models;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransactionEntry(UUID id, BigDecimal amount, TransactionType transactionType, String description, Instant createdTime) {

    public TransactionEntry(BigDecimal amount, TransactionType transactionType, String description) {
        this(UUID.randomUUID(), amount, transactionType, description, Instant.now());
    }
}
