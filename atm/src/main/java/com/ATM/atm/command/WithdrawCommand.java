package com.ATM.atm.command;

import com.ATM.atm.config.ATMConfig;
import com.ATM.atm.core.ATM;
import com.ATM.atm.entity.BankAccount;
import com.ATM.atm.enums.TransactionType;
import com.ATM.atm.exception.InsufficientCashException;
import com.ATM.atm.exception.LimitExceededException;

public class WithdrawCommand implements ATMCommand {
    private final ATM atm;
    private final BankAccount account;
    private final int amount;

    public WithdrawCommand(ATM atm, BankAccount account, int amount) {
        this.atm = atm;
        this.account = account;
        this.amount = amount;
    }

    @Override public TransactionType getType() { return TransactionType.WITHDRAWAL; }
    @Override public long getAmount() { return amount; }

    @Override
    public String execute() {
        if (amount <= 0 || amount % ATMConfig.NOTE_MULTIPLE != 0) {
            throw new IllegalArgumentException("Amount must be a positive multiple of " + ATMConfig.NOTE_MULTIPLE);
        }
        if (amount > ATMConfig.PER_TXN_LIMIT) {
            throw new LimitExceededException("Per-transaction limit is Rs." + ATMConfig.PER_TXN_LIMIT);
        }

        // 1) Pehle digital debit (thread-safe, funds/limit fail hone par exception)
        account.withdraw(amount);

        // 2) Phir physical dispense. Fail hua to rollback.
        if (!atm.getCashDispenser().dispenseCash(amount)) {
            account.deposit(amount);
            throw new InsufficientCashException();
        }
        return "Collect your cash. Remaining balance: Rs." + account.getBalance();
    }
}