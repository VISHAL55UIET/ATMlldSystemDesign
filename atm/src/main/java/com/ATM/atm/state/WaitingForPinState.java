package com.ATM.atm.state;

import com.ATM.atm.core.ATM;
import com.ATM.atm.exception.CardBlockedException;
import com.ATM.atm.strategy.AuthStrategy;

public class WaitingForPinState implements ATMState {
    private final ATM atm;

    public WaitingForPinState(ATM atm) { this.atm = atm; }

    @Override
    public void initiateTransaction(AuthStrategy a) {
        System.out.println("Session already active.");
    }

    @Override
    public void provideCredentials(String pin) {
        try {
            if (atm.getAuthStrategy().authenticate(pin)) {
                System.out.println("PIN Accepted.");
                atm.setState(atm.getAuthenticatedState());
            } else {
            int left = atm.getAuthStrategy().getRemainingAttempts();
            System.out.println("Invalid PIN. Attempts left: " + left);
        }
        } catch (CardBlockedException e) {
            System.out.println("[BLOCKED] " + e.getMessage());
            endSession();
        }
    }

    @Override
    public void requestCash(int amount) {
        System.out.println("Please enter PIN first.");
    }

    @Override
    public void requestDeposit(int amount) {
        System.out.println("Please enter PIN first.");
    }

    @Override
    public void checkBalance() {
        System.out.println("Please enter PIN first.");
    }

    @Override
    public void endSession() {
        System.out.println("Ejecting Card... Returning to Idle.\n");
        atm.resetSession();
    }
}