package com.ATM.atm.state;

import com.ATM.atm.exception.InvalidOperationException;
import com.ATM.atm.strategy.AuthStrategy;

public interface ATMState {
    void initiateTransaction(AuthStrategy authStrategy);
    void provideCredentials(String credentials);
    void requestCash(int amount);
    void endSession();

    // Default: not allowed until the user is authenticated.
    // Only AuthenticatedState overrides these two.
    default void requestDeposit(int amount) {
        throw new InvalidOperationException("Please authenticate first");
    }

    default void checkBalance() {
        throw new InvalidOperationException("Please authenticate first");
    }
}