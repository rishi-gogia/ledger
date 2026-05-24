package com.teya.ledger.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teya.ledger.models.TransactionRequest;
import com.teya.ledger.services.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static com.teya.ledger.TestUtils.transaction;
import static com.teya.ledger.models.TransactionType.DEPOSIT;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private TransactionService transactionService;

    private static final BigDecimal AMOUNT = BigDecimal.valueOf(100);

    @Test
    void testGetBalance_returnsOkWithBalance() {
        fail("not implemented");
    }

    @Test
    void testTransaction_validDeposit_returnsCreatedWithBody() throws Exception {
        // Given a valid deposit request
        final TransactionRequest request = new TransactionRequest(AMOUNT, DEPOSIT, "deposit");
        when(transactionService.addTransaction(argThat(t ->
                t.amount().equals(AMOUNT) && t.transactionType() == DEPOSIT
        ))).thenReturn(transaction(AMOUNT, DEPOSIT, "deposit"));
        // When POST /transactions is called
        // Then transaction is created with status 201 CREATED
        mockMvc.perform(post("/transactions")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value(AMOUNT))
                .andExpect(jsonPath("$.transactionType").value(DEPOSIT.toString()));
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
