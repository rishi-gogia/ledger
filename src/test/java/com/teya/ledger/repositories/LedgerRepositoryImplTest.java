package com.teya.ledger.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.teya.ledger.models.TransactionEntry;
import com.teya.ledger.models.TransactionType;

import java.math.BigDecimal;
import java.util.List;

import static com.teya.ledger.models.TransactionType.DEPOSIT;
import static com.teya.ledger.models.TransactionType.WITHDRAWAL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

class LedgerRepositoryImplTest {

    LedgerRepository ledgerRepository;

    @BeforeEach
    void setUp() {
        ledgerRepository = new LedgerRepositoryImpl();
    }

    @Test
    void testAddTransaction_deposit_updatesBalance() {
        // Given a ledger and a deposit transaction
        final TransactionEntry entry = new TransactionEntry(BigDecimal.valueOf(100), DEPOSIT, "deposit");
        // When transaction is added
        final TransactionEntry result = ledgerRepository.addTransaction(entry);
        // Then transaction is stored and balance is updated
        assertThat(result).isEqualTo(entry);
        assertThat(ledgerRepository.getTransactions()).contains(entry);
        assertThat(ledgerRepository.getBalance()).isEqualTo(BigDecimal.valueOf(100));
    }

    @Test
    void testAddTransaction_withdrawal_updatesBalance() {
        fail("not implemented");
    }

    @Test
    void testGetBalance_noTransactions_returnsZeroBalance() {
        // Given a ledger without any transactions
        // When balance is fetched
        final BigDecimal balance = ledgerRepository.getBalance();
        // Then balance is zero
        assertThat(balance).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void testGetBalance_noTransactions_returnsEmptyTransactionList() {
        // Given a ledger without any transactions
        // When transaction list is fetched
        final List<TransactionEntry> transactions = ledgerRepository.getTransactions();
        // Then the list is empty
        assertThat(transactions).isEmpty();
    }

    @Test
    void testGetBalance_withMultipleTransactions_returnsCorrectBalance() {
        fail("not implemented");
    }

    @Test
    void testGetBalance_withMultipleTransactions_returnsListOfTransactions() {
        fail("not implemented");
    }
}
