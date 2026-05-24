package com.teya.ledger.repositories;

import com.teya.ledger.models.TransactionEntry;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class LedgerRepositoryImpl implements LedgerRepository {

    @Override
    public TransactionEntry addTransaction(final TransactionEntry transaction) {
        return null;
    }

    @Override
    public List<TransactionEntry> getTransactions() {
        return null;
    }

    @Override
    public BigDecimal getBalance() {
        return null;
    }
}
