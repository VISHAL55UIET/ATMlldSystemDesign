package com.ATM.atm.exception;

public class CardBlockedException extends ATMException {
    public CardBlockedException() { super("Card is blocked after too many wrong PIN attempts"); }
}