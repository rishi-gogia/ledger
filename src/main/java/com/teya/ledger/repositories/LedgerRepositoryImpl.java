package com.teya.ledger.repositories;

import com.teya.ledger.models.TransactionEntry;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class LedgerRepositoryImpl implements LedgerRepository {

    private final Account account;

    public LedgerRepositoryImpl() {
        account = new Account();
    }

    @Override
    public TransactionEntry addTransaction(final TransactionEntry transaction) {
        final BigDecimal currentBalance = account.getBalance();
        final BigDecimal newBalance = switch (transaction.transactionType()) {
            case DEPOSIT -> currentBalance.add(transaction.amount());
            case WITHDRAWAL -> currentBalance.subtract(transaction.amount());
        };
        account.applyBalance(newBalance);
        account.addTransaction(transaction);
        return transaction;
    }

    @Override
    public List<TransactionEntry> getTransactions() {
        return account.getTransactionsList();
    }

    @Override
    public BigDecimal getBalance() {
        return account.getBalance();
    }
}
