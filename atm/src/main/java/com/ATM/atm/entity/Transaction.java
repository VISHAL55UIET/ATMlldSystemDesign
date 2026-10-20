package com.ATM.atm.entity;

import com.ATM.atm.enums.TransactionStatus;
import com.ATM.atm.enums.TransactionType;

import java.time.LocalDateTime;
import java.util.UUID;

public final class Transaction {
    private final String id = UUID.randomUUID().toString();
    private final String accountNumber;
    private final TransactionType type;
    private final long amount;
    private final TransactionStatus status;
    private final String message;
    private final LocalDateTime timestamp = LocalDateTime.now();

    public Transaction(String accountNumber, TransactionType type,
                       long amount, TransactionStatus status, String message) {
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.status = status;
        this.message = message;
    }

    public String getId() { return id; }
    public String getAccountNumber() { return accountNumber; }
    public TransactionType getType() { return type; }
    public long getAmount() { return amount; }
    public TransactionStatus getStatus() { return status; }
    public String getMessage() { return message; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return "[" + timestamp + "] " + type + " Rs." + amount + " -> " + status + " (" + message + ")";
    }
}