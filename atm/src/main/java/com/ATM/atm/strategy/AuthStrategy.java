package com.ATM.atm.strategy;

import com.ATM.atm.core.ATM;
import com.ATM.atm.entity.BankAccount;
import com.ATM.atm.state.ATMState;

public interface AuthStrategy {
    boolean authenticate(String credentials);
    BankAccount getAccount();
    ATMState getNextState(ATM atm);
    String getAuthMethodName();

    // PIN retry ke liye. UPI mein ye concept nahi hai, isliye -1.
    default int getRemainingAttempts() { return -1; }
}