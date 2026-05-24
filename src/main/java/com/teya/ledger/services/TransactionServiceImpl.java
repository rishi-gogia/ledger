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
        final BigDecimal accountBalance = ledgerRepository.getBalance();
        final BigDecimal updatedBalance = switch (transaction.transactionType()) {
            case DEPOSIT -> accountBalance.add(transaction.amount());
            case WITHDRAWAL -> accountBalance.subtract(transaction.amount());
        };
        return ledgerRepository.addTransaction(transaction, updatedBalance);
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
