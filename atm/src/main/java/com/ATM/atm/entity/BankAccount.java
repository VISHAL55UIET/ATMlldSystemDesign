package com.ATM.atm.entity;

import com.ATM.atm.config.ATMConfig;
import com.ATM.atm.exception.InsufficientFundsException;
import com.ATM.atm.exception.LimitExceededException;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {
    private final String accountNumber;
    private long balance;
    private long withdrawnToday;
    private final Lock lock = new ReentrantLock();

    // Demo only: set true to simulate processing delay and prove thread safety
    private final boolean simulateDelay;

    public BankAccount(String accountNumber, long initialBalance) {
        this(accountNumber, initialBalance, false);
    }

    public BankAccount(String accountNumber, long initialBalance, boolean simulateDelay) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
        this.simulateDelay = simulateDelay;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public long getBalance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }

    public void withdraw(long amount) {
        lock.lock();
        try {
            if (amount <= 0) {
                throw new IllegalArgumentException("Amount must be positive");
            }
            if (amount > balance) {
                throw new InsufficientFundsException();
            }
            if (withdrawnToday + amount > ATMConfig.DAILY_WITHDRAWAL_LIMIT) {
                throw new LimitExceededException("Daily withdrawal limit exceeded");
            }
            simulateProcessingDelay();
            balance -= amount;
            withdrawnToday += amount;
        } finally {
            lock.unlock();
        }
    }

    public void deposit(long amount) {
        lock.lock();
        try {
            if (amount <= 0) {
                throw new IllegalArgumentException("Amount must be positive");
            }
            balance += amount;
        } finally {
            lock.unlock();
        }
    }

    // Called by a scheduler at midnight in a real system
    public void resetDailyLimit() {
        lock.lock();
        try {
            withdrawnToday = 0;
        } finally {
            lock.unlock();
        }
    }

    private void simulateProcessingDelay() {
        if (!simulateDelay) return;
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}