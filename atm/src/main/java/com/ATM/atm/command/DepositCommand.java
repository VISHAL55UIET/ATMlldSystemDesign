package com.ATM.atm.command;

import com.ATM.atm.entity.BankAccount;
import com.ATM.atm.enums.TransactionType;

public class DepositCommand implements ATMCommand {
    private final BankAccount account;
    private final int amount;

    public DepositCommand(BankAccount account, int amount) {
        this.account = account;
        this.amount = amount;
    }

    @Override public TransactionType getType() { return TransactionType.DEPOSIT; }
    @Override public long getAmount() { return amount; }

    @Override
    public String execute() {
        account.deposit(amount);
        return "Deposited Rs." + amount + ". New balance: Rs." + account.getBalance();
    }
}