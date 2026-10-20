package com.ATM.atm.strategy;

import com.ATM.atm.core.ATM;
import com.ATM.atm.entity.BankAccount;
import com.ATM.atm.entity.Card;
import com.ATM.atm.service.BankService;
import com.ATM.atm.state.ATMState;

public class DebitCardAuth implements AuthStrategy {
    private final Card card;
    private final BankService bankService;

    public DebitCardAuth(Card card, BankService bankService) {
        this.card = card;
        this.bankService = bankService;
    }

    @Override
    public boolean authenticate(String pin) {
        return bankService.verifyCardPin(card.getCardNumber(), pin);
    }

    @Override
    public BankAccount getAccount() {
        return bankService.getAccountByCard(card.getCardNumber());
    }

    @Override
    public ATMState getNextState(ATM atm) {
        System.out.println("Card Inserted. Please enter your PIN.");
        return atm.getWaitingForPinState();
    }

    @Override
    public String getAuthMethodName() { return "Debit Card"; }

    @Override
    public int getRemainingAttempts() {
        return bankService.getRemainingPinAttempts(card.getCardNumber());
    }
}