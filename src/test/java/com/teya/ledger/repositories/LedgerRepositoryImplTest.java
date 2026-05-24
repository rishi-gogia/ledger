package com.teya.ledger.repositories;

import com.teya.ledger.models.TransactionEntry;
import com.teya.ledger.models.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.util.List;

import static com.teya.ledger.TestUtils.transaction;
import static com.teya.ledger.models.TransactionType.DEPOSIT;
import static com.teya.ledger.models.TransactionType.WITHDRAWAL;
import static org.assertj.core.api.Assertions.assertThat;

class LedgerRepositoryImplTest {

    private  static final BigDecimal AMOUNT = BigDecimal.valueOf(100);
    private  static final BigDecimal BALANCE_100 = BigDecimal.valueOf(100);
    private  static final BigDecimal BALANCE_200 = BigDecimal.valueOf(100);

    private LedgerRepository ledgerRepository;

    @BeforeEach
    void setUp() {
        ledgerRepository = new LedgerRepositoryImpl();
    }

    @ParameterizedTest
    @CsvSource({
            "100, DEPOSIT, deposit, 100",
            "100, WITHDRAWAL, withdraw, 100"
    })
    void testAddTransaction_amountUpdatesBalance(final BigDecimal amount,
                                                 final TransactionType type,
                                                 final String description,
                                                 final BigDecimal newBalance) {
        // Given a ledger
        // When Deposit Transaction
        final TransactionEntry entry = transaction(amount, type, description);
        final TransactionEntry result = ledgerRepository.addTransaction(entry, newBalance);
        // Then Transaction is done and balance is updated
        assertThat(result).isEqualTo(entry);
        assertThat(ledgerRepository.getTransactions()).contains(entry);
        assertThat(ledgerRepository.getBalance()).isEqualTo(newBalance);
    }

    @Test
    void testGetBalance_noTransaction_returnsZeroBalanceAndNoTransactions() {
        // Given a ledger without any transactions
        // When Balance and Transactions List are fetched
        final BigDecimal balance = ledgerRepository.getBalance();
        final List<TransactionEntry> transactions = ledgerRepository.getTransactions();
        // Then the balance is zero and there are no transactions
        assertThat(balance).isEqualTo(BigDecimal.ZERO);
        assertThat(transactions).isEmpty();
    }

    @Test
    void testGetBalance_someTransaction_returnsCorrectBalance() {
        // Given a ledger with some transactions
        final TransactionEntry depositEntry1 = transaction(AMOUNT, DEPOSIT, "deposit 1");
        final TransactionEntry depositEntry2 = transaction(AMOUNT, DEPOSIT, "deposit 2");
        final TransactionEntry withdrawEntry = transaction(AMOUNT, WITHDRAWAL, "withdrawal");

        ledgerRepository.addTransaction(depositEntry1, BALANCE_100);
        ledgerRepository.addTransaction(depositEntry2, BALANCE_200);
        ledgerRepository.addTransaction(withdrawEntry, BALANCE_100);
        // When Balance and Transactions List are fetched
        final BigDecimal balance = ledgerRepository.getBalance();
        final List<TransactionEntry> transactions = ledgerRepository.getTransactions();
        // Then the updated balance is retrieved and all transactions are present in the list
        assertThat(balance).isEqualTo(AMOUNT);
        assertThat(transactions).hasSize(3);
        assertThat(transactions).contains(depositEntry1, depositEntry2, withdrawEntry);
    }
}
