package com.teya.ledger.services;

import com.teya.ledger.models.TransactionEntry;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {

    TransactionEntry addTransaction(final TransactionEntry transaction);
    List<TransactionEntry> getTransactionList();
    BigDecimal getBalance();
}
