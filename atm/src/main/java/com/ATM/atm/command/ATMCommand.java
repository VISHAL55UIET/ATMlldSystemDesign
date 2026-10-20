package com.ATM.atm.command;

import com.ATM.atm.enums.TransactionType;

public interface ATMCommand {
    TransactionType getType();
    long getAmount();
    String execute();   // success message return karta hai, failure par exception throw karta hai
}