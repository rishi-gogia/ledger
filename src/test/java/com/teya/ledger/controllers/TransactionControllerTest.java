package com.teya.ledger.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.junit.jupiter.api.Assertions.fail;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Test
    void testGetBalance_returnsOkWithBalance() {
        fail("not implemented");
    }

    @Test
    void testTransaction_validDeposit_returnsCreatedWithBody() {
        fail("not implemented");
    }

    @Test
    void testTransaction_validWithdrawal_returnsCreatedWithBody() {
        fail("not implemented");
    }

    @Test
    void testTransaction_insufficientFunds_returnsUnprocessableEntity() {
        fail("not implemented");
    }

    @Test
    void testTransaction_missingAmount_returnsBadRequest() {
        fail("not implemented");
    }

    @Test
    void testTransaction_missingType_returnsBadRequest() {
        fail("not implemented");
    }

    @Test
    void testTransaction_negativeAmount_returnsBadRequest() {
        fail("not implemented");
    }

    @Test
    void testTransaction_zeroAmount_returnsBadRequest() {
        fail("not implemented");
    }

    @Test
    void testListTransactions_returnsOkWithTransactionList() {
        fail("not implemented");
    }

    @Test
    void testListTransactions_returnsEmptyList() {
        fail("not implemented");
    }
}
