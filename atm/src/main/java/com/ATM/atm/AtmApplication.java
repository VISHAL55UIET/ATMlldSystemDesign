package com.ATM.atm;

import com.ATM.atm.core.ATM;
import com.ATM.atm.factory.AuthStrategyFactory;
import com.ATM.atm.hardware.CashDispenser;
import com.ATM.atm.observer.AuditLogger;
import com.ATM.atm.observer.SmsNotifier;
import com.ATM.atm.service.BankService;
import com.ATM.atm.strategy.AuthStrategy;

public class AtmApplication {

    private static ATM newAtm(BankService bank, CashDispenser dispenser, AuditLogger audit) {
        ATM atm = new ATM(bank, dispenser);
        atm.addObserver(audit);
        atm.addObserver(new SmsNotifier());
        return atm;
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== BOOTING UP ATM SYSTEM ===");
        BankService bankService = new BankService();
        CashDispenser cashDispenser = new CashDispenser();
        AuditLogger audit = new AuditLogger();
        ATM atm = newAtm(bankService, cashDispenser, audit);

        System.out.println("\n=== SCENARIO 1: DEBIT CARD WITHDRAWAL ===");
        AuthStrategy card = AuthStrategyFactory.debitCard("CARD-123", bankService);
        atm.initiateTransaction(card);
        atm.provideCredentials("4321");
        atm.requestCash(1680);

        System.out.println("\n=== SCENARIO 2: CARDLESS UPI WITHDRAWAL ===");
        atm.initiateTransaction(AuthStrategyFactory.upi(bankService));
        atm.provideCredentials("user@upi");
        atm.requestCash(300);

        System.out.println("\n=== SCENARIO 3: BALANCE CHECK + DEPOSIT ===");
        atm.initiateTransaction(AuthStrategyFactory.upi(bankService));
        atm.provideCredentials("user@upi");
        atm.checkBalance();

        atm.initiateTransaction(AuthStrategyFactory.upi(bankService));
        atm.provideCredentials("user@upi");
        atm.requestDeposit(500);

        System.out.println("\n=== SCENARIO 4: INVALID ACTIONS (STATE PATTERN) ===");
        atm.requestCash(100);                       // Idle state mein
        atm.initiateTransaction(AuthStrategyFactory.debitCard("CARD-123", bankService));
        atm.requestCash(100);                       // PIN se pehle
        atm.provideCredentials("4321");
        atm.endSession();

        System.out.println("\n=== SCENARIO 5: WRONG PIN -> CARD BLOCK (alag BankService) ===");
        BankService freshBank = new BankService();
        ATM atm2 = newAtm(freshBank, new CashDispenser(), audit);
        atm2.initiateTransaction(AuthStrategyFactory.debitCard("CARD-123", freshBank));
        atm2.provideCredentials("0000");
        atm2.provideCredentials("1111");
        atm2.provideCredentials("2222");            // 3rd wrong -> blocked

        System.out.println("\n=== SCENARIO 6: CONCURRENCY (RACE CONDITION) ===");
        // Balance ab 5000 - 1680 - 300 + 500 = 3520.
        // Dono threads Rs.3000 maangte hain, sirf ek succeed hona chahiye.
        Runnable task = () -> {
            ATM threadAtm = newAtm(bankService, cashDispenser, audit);
            System.out.println(Thread.currentThread().getName() + " attempting Rs.3000...");
            threadAtm.initiateTransaction(AuthStrategyFactory.upi(bankService));
            threadAtm.provideCredentials("user@upi");
            threadAtm.requestCash(3000);
        };
        Thread t1 = new Thread(task, "[ATM Machine A]");
        Thread t2 = new Thread(task, "[Online Auto-Pay]");
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("\n=== AUDIT HISTORY (" + audit.getHistory().size() + " transactions) ===");
        audit.getHistory().forEach(System.out::println);
    }
}