package com.ATM.atm.core;

import com.ATM.atm.entity.BankAccount;
import com.ATM.atm.entity.Transaction;
import com.ATM.atm.hardware.CashDispenser;
import com.ATM.atm.observer.TransactionObserver;
import com.ATM.atm.service.BankService;
import com.ATM.atm.state.*;
import com.ATM.atm.strategy.AuthStrategy;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ATM {
    private final ATMState idleState;
    private final ATMState waitingForPinState;
    private final ATMState waitingForUpiScanState;
    private final ATMState authenticatedState;

    private ATMState currentState;
    private AuthStrategy authStrategy;

    // Session data: who is logged in right now
    private BankAccount currentAccount;

    private final BankService bankService;
    private final CashDispenser cashDispenser;

    // Observers (audit log, SMS, etc.)
    private final List<TransactionObserver> observers = new CopyOnWriteArrayList<>();

    public ATM(BankService bankService, CashDispenser cashDispenser) {
        this.idleState = new IdleState(this);
        this.waitingForPinState = new WaitingForPinState(this);
        this.waitingForUpiScanState = new WaitingForUpiScanState(this);
        this.authenticatedState = new AuthenticatedState(this);

        this.currentState = idleState;
        this.bankService = bankService;
        this.cashDispenser = cashDispenser;
    }

    // ---------- Pass-through state methods ----------
    public void initiateTransaction(AuthStrategy strategy) { currentState.initiateTransaction(strategy); }
    public void provideCredentials(String credentials)     { currentState.provideCredentials(credentials); }
    public void requestCash(int amount)                    { currentState.requestCash(amount); }
    public void requestDeposit(int amount)                 { currentState.requestDeposit(amount); }
    public void checkBalance()                             { currentState.checkBalance(); }
    public void endSession()                               { currentState.endSession(); }

    // ---------- Session handling ----------
    public BankAccount getCurrentAccount() { return currentAccount; }
    public void setCurrentAccount(BankAccount account) { this.currentAccount = account; }

    // Internal cleanup: states call this when a session finishes
    public void resetSession() {
        this.currentAccount = null;
        this.authStrategy = null;
        this.currentState = idleState;
    }

    // ---------- Observer support ----------
    public void addObserver(TransactionObserver observer) { observers.add(observer); }

    public void notifyObservers(Transaction transaction) {
        for (TransactionObserver o : observers) {
            o.onTransaction(transaction);
        }
    }

    // ---------- Getters and setters ----------
    public void setState(ATMState state) { this.currentState = state; }
    public ATMState getIdleState() { return idleState; }
    public ATMState getWaitingForPinState() { return waitingForPinState; }
    public ATMState getWaitingForUpiScanState() { return waitingForUpiScanState; }
    public ATMState getAuthenticatedState() { return authenticatedState; }

    public void setAuthStrategy(AuthStrategy strategy) { this.authStrategy = strategy; }
    public AuthStrategy getAuthStrategy() { return authStrategy; }

    public BankService getBankService() { return bankService; }
    public CashDispenser getCashDispenser() { return cashDispenser; }
}