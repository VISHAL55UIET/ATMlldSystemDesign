package com.ATM.atm.service;

import com.ATM.atm.config.ATMConfig;
import com.ATM.atm.entity.BankAccount;
import com.ATM.atm.exception.ATMException;
import com.ATM.atm.exception.CardBlockedException;
import com.ATM.atm.repository.AccountRepository;
import com.ATM.atm.repository.InMemoryAccountRepository;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BankService {
    private final AccountRepository accountRepository;

    // PIN data stays in the bank layer (the ATM never stores PINs)
    private final Map<String, String> cardPins = new ConcurrentHashMap<>();
    private final Map<String, Integer> failedAttempts = new ConcurrentHashMap<>();
    private final Set<String> blockedCards = ConcurrentHashMap.newKeySet();

    // Default: in-memory DB seeded with mock data (same data as before)
    public BankService() {
        InMemoryAccountRepository repo = new InMemoryAccountRepository();

        repo.save(new BankAccount("ACC-1001", 50_000L));
        repo.linkCard("CARD-123", "ACC-1001");
        repo.linkUpi("user@upi", "ACC-1001");

        this.accountRepository = repo;
        this.cardPins.put("CARD-123", "4321");
    }

    // Constructor injection: lets you pass a different repository in tests
    public BankService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public boolean verifyCardPin(String cardNumber, String pin) {
        if (blockedCards.contains(cardNumber)) {
            throw new CardBlockedException();
        }

        String expectedPin = cardPins.get(cardNumber);
        if (expectedPin != null && expectedPin.equals(pin)) {
            failedAttempts.remove(cardNumber);   // reset counter on success
            return true;
        }

        int attempts = failedAttempts.merge(cardNumber, 1, Integer::sum);
        if (attempts >= ATMConfig.MAX_PIN_ATTEMPTS) {
            blockedCards.add(cardNumber);
            throw new CardBlockedException();
        }
        return false;
    }

    public int getRemainingPinAttempts(String cardNumber) {
        return ATMConfig.MAX_PIN_ATTEMPTS - failedAttempts.getOrDefault(cardNumber, 0);
    }

    public BankAccount getAccountByCard(String cardNumber) {
        return accountRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new ATMException("Card not registered: " + cardNumber));
    }

    public boolean verifyUpiScan(String upiId) {
        // In reality this pings the UPI gateway to verify a dynamic QR scan.
        return accountRepository.findByUpiId(upiId).isPresent();
    }

    public BankAccount getAccountByUpi(String upiId) {
        return accountRepository.findByUpiId(upiId)
                .orElseThrow(() -> new ATMException("UPI ID not registered: " + upiId));
    }
}