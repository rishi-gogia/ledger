package com.teya.ledger.repositories;

import com.teya.ledger.models.TransactionEntry;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class Account {

    @Getter
    private final UUID id;
    @Getter
    private final String name;
    private final List<TransactionEntry> transactions;
    @Getter
    private BigDecimal balance;

    public Account() {
        this.id = UUID.randomUUID();
        this.name = "Account name";
        this.transactions = new CopyOnWriteArrayList<>();
        this.balance = BigDecimal.ZERO;
    }

    public List<TransactionEntry> getTransactionsList() {
        return Collections.unmodifiableList(transactions);
    }

    void addTransaction(TransactionEntry transaction) {
        transactions.add(transaction);
    }

    void applyBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
