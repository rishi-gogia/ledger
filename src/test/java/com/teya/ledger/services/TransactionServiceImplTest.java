package com.teya.ledger.services;

import com.teya.ledger.models.TransactionEntry;
import com.teya.ledger.repositories.LedgerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static com.teya.ledger.TestUtils.transaction;
import static com.teya.ledger.models.TransactionType.DEPOSIT;
import static com.teya.ledger.models.TransactionType.WITHDRAWAL;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    private static final BigDecimal INITIAL_BALANCE = BigDecimal.valueOf(1000);
    private static final BigDecimal AMOUNT = BigDecimal.valueOf(100);

    @Mock
    private LedgerRepository ledgerRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    void testAddTransaction_deposit_updatesBalance() {
        // Given an account with initial balance
        when(ledgerRepository.getBalance()).thenReturn(INITIAL_BALANCE);
        // When a deposit transaction is made
        final TransactionEntry deposit = transaction(AMOUNT, DEPOSIT, "deposit");
        transactionService.addTransaction(deposit);
        // Then ledger is updated with the correct new balance
        verify(ledgerRepository).addTransaction(deposit, BigDecimal.valueOf(1100));
    }

    @Test
    void testAddTransaction_withdrawal_updatesBalance() {
        // Given an account with initial balance
        when(ledgerRepository.getBalance()).thenReturn(INITIAL_BALANCE);
        // When a withdrawal transaction is made
        final TransactionEntry withdrawal = transaction(AMOUNT, WITHDRAWAL, "withdrawal");
        transactionService.addTransaction(withdrawal);
        // Then ledger is updated with the correct new balance
        verify(ledgerRepository).addTransaction(withdrawal, BigDecimal.valueOf(900));
    }

    @Test
    void testAddTransaction_withdrawal_exactBalance_succeeds() {
        fail("not implemented");
    }

    @Test
    void testAddTransaction_withdrawal_insufficientFunds_throwsException() {
        fail("not implemented");
    }

    @Test
    void testAddTransaction_withdrawal_onEmptyLedger_throwsException() {
        fail("not implemented");
    }

    @Test
    void testGetTransactionList_returnsTransactionList() {
        fail("not implemented");
    }

    @Test
    void testGetTransactionList_returnsEmptyList() {
        fail("not implemented");
    }

    @Test
    void testGetBalance_returnsBalance() {
        fail("not implemented");
    }
}
