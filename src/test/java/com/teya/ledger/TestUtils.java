package com.teya.ledger;

import com.teya.ledger.models.TransactionEntry;
import com.teya.ledger.models.TransactionType;

import java.math.BigDecimal;

public class TestUtils {

    public static TransactionEntry transaction(final BigDecimal amount, final TransactionType type, final String description) {
        return new TransactionEntry(amount, type, description);
    }
}
