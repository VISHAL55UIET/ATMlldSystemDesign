package com.ATM.atm.exception;


public class InsufficientCashException extends ATMException {
    public InsufficientCashException() { super("ATM does not have enough cash / cannot dispense this amount"); }
}