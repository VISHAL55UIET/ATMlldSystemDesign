package com.ATM.atm.observer;

import com.ATM.atm.entity.Transaction;
import com.ATM.atm.enums.TransactionStatus;
import com.ATM.atm.enums.TransactionType;

public class SmsNotifier implements TransactionObserver {
    @Override
    public void onTransaction(Transaction t) {
        if (t.getType() == TransactionType.BALANCE_INQUIRY) return; // no SMS for balance checks
        String result = t.getStatus() == TransactionStatus.SUCCESS ? "successful" : "failed";
        System.out.println("[SMS] A/c " + t.getAccountNumber() + ": "
                + t.getType() + " of Rs." + t.getAmount() + " " + result);
    }
}