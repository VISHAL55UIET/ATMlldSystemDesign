package com.ATM.atm.exception;

public class InsufficientFundsException extends ATMException {
    public InsufficientFundsException() { super("Insufficient account balance"); }
}