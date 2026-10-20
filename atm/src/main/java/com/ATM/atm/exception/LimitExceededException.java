package com.ATM.atm.exception;

public class LimitExceededException extends ATMException {
    public LimitExceededException(String message) { super(message); }
}