package com.teya.ledger.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransactionRequest(@NotNull @Positive BigDecimal amount, @NotNull TransactionType transactionType, String description) {}
