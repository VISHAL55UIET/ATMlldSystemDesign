package com.ATM.atm.factory;

import com.ATM.atm.entity.Card;
import com.ATM.atm.enums.AuthType;
import com.ATM.atm.service.BankService;
import com.ATM.atm.strategy.AuthStrategy;
import com.ATM.atm.strategy.DebitCardAuth;
import com.ATM.atm.strategy.UpiAuth;

public final class AuthStrategyFactory {
    private AuthStrategyFactory() {}

    public static AuthStrategy debitCard(String cardNumber, BankService bank) {
        return new DebitCardAuth(new Card(cardNumber), bank);
    }

    public static AuthStrategy upi(BankService bank) {
        return new UpiAuth(bank);
    }

    public static AuthStrategy create(AuthType type, String cardNumber, BankService bank) {
        return switch (type) {
            case DEBIT_CARD -> debitCard(cardNumber, bank);
            case UPI        -> upi(bank);
        };
    }
}