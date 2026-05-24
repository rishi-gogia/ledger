package com.teya.ledger.services;

import com.teya.ledger.models.TransactionEntry;
import com.teya.ledger.repositories.LedgerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final LedgerRepository ledgerRepository;

    @Override
    public TransactionEntry addTransaction(final TransactionEntry transaction) {
        return null;
    }

    @Override
    public List<TransactionEntry> getTransactionList() {
        return null;
    }

    @Override
    public BigDecimal getBalance() {
        return null;
    }
}
