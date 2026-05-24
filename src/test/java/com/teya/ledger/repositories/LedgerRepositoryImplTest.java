package com.teya.ledger.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.teya.ledger.models.TransactionEntry;

import java.math.BigDecimal;
import java.util.List;

import static com.teya.ledger.models.TransactionType.DEPOSIT;
import static com.teya.ledger.models.TransactionType.WITHDRAWAL;

import static org.assertj.core.api.Assertions.assertThat;

class LedgerRepositoryImplTest {

    private static final BigDecimal AMOUNT = BigDecimal.valueOf(100);
    private static final BigDecimal WITHDRAWAL_AMOUNT = BigDecimal.valueOf(50);

    LedgerRepository ledgerRepository;

    @BeforeEach
    void setUp() {
        ledgerRepository = new LedgerRepositoryImpl();
    }

    @Test
    void testAddTransaction_deposit_updatesBalance() {
        // Given a ledger and a deposit transaction
        final TransactionEntry entry = new TransactionEntry(AMOUNT, DEPOSIT, "deposit");
        // When transaction is added
        final TransactionEntry result = ledgerRepository.addTransaction(entry);
        // Then transaction is stored and balance is updated
        assertThat(result).isEqualTo(entry);
        assertThat(ledgerRepository.getTransactions()).contains(entry);
        assertThat(ledgerRepository.getBalance()).isEqualTo(AMOUNT);
    }

    @Test
    void testAddTransaction_withdrawal_updatesBalance() {
        // Given a ledger with an existing balance
        ledgerRepository.addTransaction(new TransactionEntry(AMOUNT, DEPOSIT, "deposit"));
        // When a withdrawal transaction is added
        final TransactionEntry entry = new TransactionEntry(BigDecimal.valueOf(40), WITHDRAWAL, "withdrawal");
        final TransactionEntry result = ledgerRepository.addTransaction(entry);
        // Then transaction is stored and balance is updated
        assertThat(result).isEqualTo(entry);
        assertThat(ledgerRepository.getTransactions()).contains(entry);
        assertThat(ledgerRepository.getBalance()).isEqualTo(BigDecimal.valueOf(60));
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
        // Given a ledger with multiple transactions
        ledgerRepository.addTransaction(new TransactionEntry(AMOUNT, DEPOSIT, "deposit 1"));
        ledgerRepository.addTransaction(new TransactionEntry(AMOUNT, DEPOSIT, "deposit 2"));
        ledgerRepository.addTransaction(new TransactionEntry(WITHDRAWAL_AMOUNT, WITHDRAWAL, "withdrawal"));
        // When balance is fetched
        final BigDecimal balance = ledgerRepository.getBalance();
        // Then balance reflects all transactions
        assertThat(balance).isEqualTo(BigDecimal.valueOf(150));
    }

    @Test
    void testGetBalance_withMultipleTransactions_returnsListOfTransactions() {
        // Given a ledger with multiple transactions
        final TransactionEntry deposit1 = new TransactionEntry(AMOUNT, DEPOSIT, "deposit 1");
        final TransactionEntry deposit2 = new TransactionEntry(AMOUNT, DEPOSIT, "deposit 2");
        final TransactionEntry withdrawal = new TransactionEntry(WITHDRAWAL_AMOUNT, WITHDRAWAL, "withdrawal");
        ledgerRepository.addTransaction(deposit1);
        ledgerRepository.addTransaction(deposit2);
        ledgerRepository.addTransaction(withdrawal);
        // When transaction list is fetched
        final List<TransactionEntry> transactions = ledgerRepository.getTransactions();
        // Then all transactions are present
        assertThat(transactions).hasSize(3);
        assertThat(transactions).contains(deposit1, deposit2, withdrawal);
    }
}
