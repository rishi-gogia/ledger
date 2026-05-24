package com.teya.ledger.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teya.ledger.exceptions.InsufficientFundsException;
import com.teya.ledger.models.TransactionEntry;
import com.teya.ledger.models.TransactionRequest;
import com.teya.ledger.models.TransactionType;
import com.teya.ledger.services.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static com.teya.ledger.TestUtils.transaction;
import static com.teya.ledger.models.TransactionType.DEPOSIT;
import static com.teya.ledger.models.TransactionType.WITHDRAWAL;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    void testGetBalance_returnsOkWithBalance() throws Exception {
        // Given an account with a certain balance
        when(transactionService.getBalance()).thenReturn(AMOUNT);
        // When GET /balance is called
        // Then the balance is returned with status 200 OK
        mockMvc.perform(get("/balance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(AMOUNT));
    }

    @ParameterizedTest
    @CsvSource({
            "1000, DEPOSIT, deposit",
            "1000, WITHDRAWAL, withdrawal",
            "1000, DEPOSIT,"
    })
    void testTransaction_validTransaction_returnsCreatedWithBody(final BigDecimal amount,
                                                                 final TransactionType transactionType,
                                                                 final String description) throws Exception {
        // Given a valid transaction request
        final TransactionRequest request = new TransactionRequest(amount, transactionType, description);
        when(transactionService.addTransaction(argThat(t ->
                t.amount().equals(amount) && t.transactionType() == transactionType
        ))).thenReturn(transaction(amount, transactionType, description));
        // When POST /transactions is called
        // Then transaction is created with status 201 CREATED
        mockMvc.perform(post("/transactions")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value(amount))
                .andExpect(jsonPath("$.transactionType").value(transactionType.toString()));
    }

    @Test
    void testTransaction_insufficientFunds_returnsUnprocessableEntity() throws Exception {
        // Given a withdrawal that exceeds the balance
        final TransactionRequest request = new TransactionRequest(AMOUNT, WITHDRAWAL, "withdrawal");
        when(transactionService.addTransaction(argThat(t ->
                t.amount().equals(AMOUNT) && t.transactionType() == WITHDRAWAL
        ))).thenThrow(new InsufficientFundsException("Insufficient funds"));
        // When POST /transactions is called
        // Then 422 Unprocessable Entity is returned with error body
        mockMvc.perform(post("/transactions")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.error").value("INSUFFICIENT_FUNDS"))
                .andExpect(jsonPath("$.description").value("Insufficient funds"));
    }

    @ParameterizedTest
    @MethodSource("invalidRequests")
    void testTransaction_invalidRequest_returnsBadRequest(final String body) throws Exception {
        // Given an invalid transaction request
        // When POST /transactions is called
        // Then 400 Bad Request is returned with VALIDATION_ERROR
        mockMvc.perform(post("/transactions")
                        .contentType(APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));
    }

    private static Stream<String> invalidRequests() {
        return Stream.of(
                // No amount
                """
                {
                    "transactionType": "DEPOSIT",
                    "description": "some description"
                }""",
                // No type
                """
                {
                    "amount": "100",
                    "description": "some description"
                }""",
                // Negative amount
                """
                {
                    "amount": "-100",
                    "transactionType": "DEPOSIT",
                    "description": "some description"
                }""",
                // Zero amount
                """
                {
                    "amount": "0",
                    "transactionType": "DEPOSIT",
                    "description": "some description"
                }"""
        );
    }

    @Test
    void testListTransactions_returnsOkWithTransactionList() throws Exception {
        // Given an account with transactions
        final List<TransactionEntry> transactionList = List.of(
                transaction(BigDecimal.TEN, DEPOSIT, "deposit 1"),
                transaction(BigDecimal.ONE, WITHDRAWAL, "withdrawal 1")
        );
        when(transactionService.getTransactionList()).thenReturn(transactionList);
        // When GET /transactions is called
        // Then the full transaction list is returned with status 200 OK
        mockMvc.perform(get("/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].transactionType").value(DEPOSIT.toString()))
                .andExpect(jsonPath("$[1].transactionType").value(WITHDRAWAL.toString()));
    }

    @Test
    void testListTransactions_returnsEmptyList() throws Exception {
        // Given an account with no transactions
        when(transactionService.getTransactionList()).thenReturn(List.of());
        // When GET /transactions is called
        // Then an empty list is returned with status 200 OK
        mockMvc.perform(get("/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }
}
