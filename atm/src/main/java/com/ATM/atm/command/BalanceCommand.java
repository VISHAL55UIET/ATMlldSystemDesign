package com.ATM.atm.command;

import com.ATM.atm.entity.BankAccount;
import com.ATM.atm.enums.TransactionType;

public class BalanceCommand implements ATMCommand {
    private final BankAccount account;

    public BalanceCommand(BankAccount account) { this.account = account; }

    @Override public TransactionType getType() { return TransactionType.BALANCE_INQUIRY; }
    @Override public long getAmount() { return 0; }

    @Override
    public String execute() {
        return "Available balance: Rs." + account.getBalance();
    }
}