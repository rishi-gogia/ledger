package com.teya.ledger.repositories;

import com.teya.ledger.models.TransactionEntry;

import java.math.BigDecimal;
import java.util.List;

public interface LedgerRepository {

    TransactionEntry addTransaction(final TransactionEntry transaction, final BigDecimal updatedBalance);
    List<TransactionEntry> getTransactions();
    BigDecimal getBalance();
}
