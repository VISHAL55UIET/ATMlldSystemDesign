package com.ATM.atm.state;

import com.ATM.atm.core.ATM;
import com.ATM.atm.strategy.AuthStrategy;

public class IdleState implements ATMState {
    private final ATM atm;

    public IdleState(ATM atm) { this.atm = atm; }

    @Override
    public void initiateTransaction(AuthStrategy authStrategy) {
        atm.setAuthStrategy(authStrategy);
        System.out.println("Initiating " + authStrategy.getAuthMethodName() + " transaction...");

        atm.setState(authStrategy.getNextState(atm));
    }

    @Override
    public void provideCredentials(String c) {
        System.out.println("Please initiate a transaction first.");
    }

    @Override
    public void requestCash(int a) {
        System.out.println("Please initiate a transaction first.");
    }

    @Override
    public void requestDeposit(int a) {
        System.out.println("Please initiate a transaction first.");
    }

    @Override
    public void checkBalance() {
        System.out.println("Please initiate a transaction first.");
    }

    @Override
    public void endSession() {
        System.out.println("No active session.");
    }
}