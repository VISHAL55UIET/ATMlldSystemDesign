package com.ATM.atm.strategy;


import com.ATM.atm.core.ATM;
import com.ATM.atm.entity.BankAccount;
import com.ATM.atm.exception.ATMException;
import com.ATM.atm.service.BankService;
import com.ATM.atm.state.ATMState;

public class UpiAuth implements AuthStrategy {
    private final BankService bankService;
    private BankAccount verifiedAccount;

    public UpiAuth(BankService bankService) {
        this.bankService = bankService;
    }

    @Override
    public boolean authenticate(String upiId) {
        try {
            this.verifiedAccount = bankService.getAccountByUpi(upiId);
            return true;
        } catch (ATMException e) {
            return false;
        }
    }

    @Override
    public BankAccount getAccount() {
        return verifiedAccount;
    }

    @Override
    public ATMState getNextState(ATM atm) {
        System.out.println("Please scan the QR code using your UPI App.");
        return atm.getWaitingForUpiScanState();
    }

    @Override
    public String getAuthMethodName() { return "UPI"; }
}