package com.ATM.atm.exception;


public class InvalidPinException extends ATMException {
    private final int attemptsLeft;

    public InvalidPinException(int attemptsLeft) {
        super("Invalid PIN. Attempts left: " + attemptsLeft);
        this.attemptsLeft = attemptsLeft;
    }
    public int getAttemptsLeft() { return attemptsLeft; }
}