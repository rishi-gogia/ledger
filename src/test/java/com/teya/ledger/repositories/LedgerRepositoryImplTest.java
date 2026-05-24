package com.teya.ledger.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

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
        fail("not implemented");
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
        fail("not implemented");
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
