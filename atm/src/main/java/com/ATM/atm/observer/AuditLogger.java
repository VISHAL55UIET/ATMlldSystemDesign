package com.ATM.atm.observer;

import com.ATM.atm.entity.Transaction;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AuditLogger implements TransactionObserver {
    private final List<Transaction> history = new CopyOnWriteArrayList<>();

    @Override
    public void onTransaction(Transaction t) {
        history.add(t);
        System.out.println("[AUDIT] " + t);
    }

    public List<Transaction> getHistory() {
        return List.copyOf(history);
    }
}