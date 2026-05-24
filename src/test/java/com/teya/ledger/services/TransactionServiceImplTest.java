package com.teya.ledger.services;

import com.teya.ledger.exceptions.InsufficientFundsException;
import com.teya.ledger.models.TransactionEntry;
import com.teya.ledger.models.TransactionType;
import com.teya.ledger.repositories.LedgerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static com.teya.ledger.TestUtils.transaction;
import static com.teya.ledger.models.TransactionType.DEPOSIT;
import static com.teya.ledger.models.TransactionType.WITHDRAWAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    private static final BigDecimal AMOUNT = BigDecimal.valueOf(100);
    private static final String DESCRIPTION_DEPOSIT = "deposit";
    private static final String DESCRIPTION_WITHDRAW = "withdraw";
    private static final String DESCRIPTION_INVALID = "Invalid amount";

    @Mock
    private LedgerRepository ledgerRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @ParameterizedTest
    @CsvSource({
            "1000, 100, DEPOSIT, depositing, 1100",
            "1000, 100, WITHDRAWAL, withdrawing, 900",
            "1000, 1000, WITHDRAWAL, withdrawing, 0",
            "0, 100, DEPOSIT, depositing, 100"
    })
    void testAddTransaction_transact_validAmountUpdatesBalance(final BigDecimal initialBalance,
                                                               final BigDecimal amount,
                                                               final TransactionType transactionType,
                                                               final String description,
                                                               final BigDecimal balance) {
        // Given an account with some initial balance
        when(ledgerRepository.getBalance()).thenReturn(initialBalance);
        // When a valid transaction happens
        final TransactionEntry entry = transaction(amount, transactionType, description);
        transactionService.addTransaction(entry);
        // Then the ledger repo is called to update the balance
        verify(ledgerRepository).addTransaction(entry, balance);
    }

    @ParameterizedTest
    @CsvSource({
            "1000, 1100",
            "0, 1"
    })
    void testAddTransaction_withdraw_insufficientBalanceThrowsException(final BigDecimal initialBalance,
                                                                        final BigDecimal withdrawAmount) {
        // Given an account with initial balance
        when(ledgerRepository.getBalance()).thenReturn(initialBalance);
        // When a withdrawal transaction exceeds the balance
        final TransactionEntry withdraw = transaction(withdrawAmount, WITHDRAWAL, DESCRIPTION_INVALID);
        // Then an InsufficientFundsException is thrown
        assertThrows(InsufficientFundsException.class, () -> transactionService.addTransaction(withdraw));
    }

    @Test
    void testGetTransactionList_returnsTransactionList() {
        // Given a list of transactions performed in an account
        final List<TransactionEntry> transactionEntries = List.of(
                transaction(AMOUNT, DEPOSIT, DESCRIPTION_DEPOSIT),
                transaction(AMOUNT, DEPOSIT, DESCRIPTION_DEPOSIT),
                transaction(AMOUNT, WITHDRAWAL, DESCRIPTION_WITHDRAW)
        );
        when(ledgerRepository.getTransactions()).thenReturn(transactionEntries);
        // When transaction list is fetched
        final List<TransactionEntry> result = transactionService.getTransactionList();
        // Then transactions list is retrieved fully
        assertThat(result).hasSize(transactionEntries.size());
        assertThat(result).containsAll(transactionEntries);
    }

    @Test
    void testGetTransactionList_returnsEmptyList() {
        // Given there are no transactions for an account
        when(ledgerRepository.getTransactions()).thenReturn(List.of());
        // When transaction list is fetched
        final List<TransactionEntry> result = transactionService.getTransactionList();
        // Then no transactions are present in the result
        assertThat(result).isEmpty();
    }

    @Test
    void testGetBalance_returnsBalance() {
        // Given there is an account with a certain balance
        when(ledgerRepository.getBalance()).thenReturn(AMOUNT);
        // When the balance is fetched
        final BigDecimal result = transactionService.getBalance();
        // Then the correct balance is retrieved
        assertThat(result).isEqualTo(AMOUNT);
    }
}
