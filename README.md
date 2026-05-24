# Ledger API

A simple in-memory ledger REST API built with Java 17 and Spring Boot 3.2.

## Prerequisites

- Java 17+

No other software installation required.

## How to Run

**Linux / macOS / WSL:**
#### Make sure to give execute permission to mvnw
```bash
chmod +x mvnw
```
#### Run the application
```bash
./mvnw spring-boot:run
```

**Windows:**
```bash
mvnw.cmd spring-boot:run
```

The server starts on `http://localhost:8080`.

To run the tests:

**Linux / macOS / WSL:**
```bash
./mvnw test
```

**Windows:**
```bash
mvnw.cmd test
```

---

## API Endpoints

### Record a transaction

```
POST /transactions
Content-Type: application/json
```

**Deposit:**
```bash
curl -s -X POST http://localhost:8080/transactions \
  -H "Content-Type: application/json" \
  -d '{"amount": 500.00, "type": "DEPOSIT", "description": "Initial deposit"}'
```

```json
{
  "id": "a1b2c3d4-...",
  "amount": 500.00,
  "transactionType": "DEPOSIT",
  "createdTime": "2026-05-24T10:00:00Z",
  "description": "Initial deposit"
}
```

**Withdrawal:**
```bash
curl -s -X POST http://localhost:8080/transactions \
  -H "Content-Type: application/json" \
  -d '{"amount": 100.00, "type": "WITHDRAWAL", "description": "Coffee"}'
```

```json
{
  "id": "b2c3d4e5-...",
  "amount": 100.00,
  "transactionType": "WITHDRAWAL",
  "createdTime": "2026-05-24T10:01:00Z",
  "description": "Coffee"
}
```

**Insufficient funds (422):**
```bash
curl -s -o /dev/null -w "%{http_code}" -X POST http://localhost:8080/transactions \
  -H "Content-Type: application/json" \
  -d '{"amount": 9999.00, "type": "WITHDRAWAL"}'
```
```
422
```

**Invalid request (400) — missing type:**
```bash
curl -s -o /dev/null -w "%{http_code}" -X POST http://localhost:8080/transactions \
  -H "Content-Type: application/json" \
  -d '{"amount": 100.00}'
```
```
400
```

---

### View current balance

```
GET /balance
```

```bash
curl -s http://localhost:8080/balance
```

```json
{ "balance": 400.00 }
```

---

### View transaction history

```
GET /transactions
```

```bash
curl -s http://localhost:8080/transactions
```

```json
[
  {
    "id": "a1b2c3d4-...",
    "amount": 500.00,
    "transactionType": "DEPOSIT",
    "createdTime": "2026-05-24T10:00:00Z",
    "description": "Initial deposit"
  },
  {
    "id": "b2c3d4e5-...",
    "amount": 100.00,
    "transactionType": "WITHDRAWAL",
    "createdTime": "2026-05-24T10:01:00Z",
    "description": "Coffee"
  }
]
```

---

## Assumptions

1. **Single account** — one account per running instance. No multi-account support. Extension path: key storage by account ID; transfers modelled as compensating entry pairs.

2. **No persistence** — data is held in memory and lost on restart. The spec requires in-memory data structures; this is the accepted trade-off.

3. **No overdraft** — a withdrawal that would result in a negative balance is rejected with `422 Unprocessable Entity`. No overdraft facility or interest accrual is modelled; the ledger records movements only, not financial products built on top of them.

4. **Append-only** — transactions cannot be edited or deleted. Corrections are recorded as new compensating entries, preserving full audit trail integrity.

5. **Amounts must be positive and non-zero** — validated on every transaction. Enforced via `@Positive` constraint on the request.

6. **Optional description** — `description` is not required by the spec but included as a useful enrichment field. May be null or omitted.

7. **Currency** — amounts are decimal values treated as units of a single currency (e.g. GBP). `BigDecimal` is used throughout to avoid floating-point precision errors. No multi-currency support.

8. **No idempotency** — duplicate POST requests produce duplicate transactions. In production this would be addressed with a client-supplied idempotency key checked on receipt. Documented as a known limitation.

9. **No pagination, filtering, or sorting** — `GET /transactions` returns the full history in insertion order (oldest first). Insertion order preserves audit trail integrity and is the natural read order for an append-only ledger. Pagination and date-range filtering are out of scope for this demo.

10. **Concurrency** — though the spec lists atomic operations as not required, a `synchronized` write guard was added to the service layer to prevent balance corruption if concurrent requests arrive. `CopyOnWriteArrayList` is used for the transaction list to ensure concurrent reads are safe. The trade-off: `CopyOnWriteArrayList` copies the array on every write — acceptable at demo scale, not for write-heavy production load.

---

## Design Decisions

**Single `POST /transactions` endpoint** rather than separate `/deposit` and `/withdrawal` endpoints. The transaction type is carried in the request body as `DEPOSIT | WITHDRAWAL`. This is more extensible (adding a `TRANSFER` type later is a field change, not an endpoint change) and aligns with how ledger APIs are typically modelled in fintech.

**Balance is cached, not computed on read.** The service computes the new balance on every write and passes it atomically to the repository, which stores it as a single field. `GET /balance` is O(1). The alternative — summing all transactions on every read — is O(n) and degrades as history grows. Both approaches are correct for in-memory demo scale; the cached approach is more production-aligned.
