package com.ATM.atm.observer;

import com.ATM.atm.entity.Transaction;

public interface TransactionObserver {
    void onTransaction(Transaction transaction);
}