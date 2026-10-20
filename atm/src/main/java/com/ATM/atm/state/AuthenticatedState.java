package com.ATM.atm.state;

import com.ATM.atm.command.ATMCommand;
import com.ATM.atm.command.BalanceCommand;
import com.ATM.atm.command.DepositCommand;
import com.ATM.atm.command.WithdrawCommand;
import com.ATM.atm.core.ATM;
import com.ATM.atm.entity.BankAccount;
import com.ATM.atm.entity.Transaction;
import com.ATM.atm.enums.TransactionStatus;
import com.ATM.atm.exception.ATMException;
import com.ATM.atm.strategy.AuthStrategy;

public class AuthenticatedState implements ATMState {
    private final ATM atm;

    public AuthenticatedState(ATM atm) { this.atm = atm; }

    @Override public void initiateTransaction(AuthStrategy a) { System.out.println("Already authenticated."); }
    @Override public void provideCredentials(String c) { System.out.println("Already authenticated."); }

    @Override
    public void requestCash(int amount) {
        run(new WithdrawCommand(atm, account(), amount));
    }

    @Override
    public void requestDeposit(int amount) {
        if (amount <= 0) {
            System.out.println("Invalid deposit amount.\n");
            endSession();
            return;
        }
        run(new DepositCommand(account(), amount));
    }

    @Override
    public void checkBalance() {
        run(new BalanceCommand(account()));
    }

    @Override
    public void endSession() {
        System.out.println("Ending session securely. Returning to Idle.\n");
        atm.resetSession();
    }

    private BankAccount account() {
        return atm.getAuthStrategy().getAccount();
    }

    // Common flow: execute -> Transaction banao -> observers ko batao -> session band
    private void run(ATMCommand command) {
        BankAccount account = account();
        TransactionStatus status;
        String message;
        try {
            message = command.execute();
            status = TransactionStatus.SUCCESS;
        } catch (ATMException | IllegalArgumentException e) {
            message = e.getMessage();
            status = TransactionStatus.FAILED;
        }

        System.out.println(atm_label(status) + message);
        atm.notifyObservers(new Transaction(
                account.getAccountNumber(), command.getType(), command.getAmount(), status, message));
        endSession();
    }

    private String atm_label(TransactionStatus s) {
        return s == TransactionStatus.SUCCESS ? "[OK] " : "[FAILED] ";
    }
}