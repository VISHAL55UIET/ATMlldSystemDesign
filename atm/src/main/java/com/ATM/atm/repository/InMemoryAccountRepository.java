package com.ATM.atm.repository;

import com.ATM.atm.entity.BankAccount;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAccountRepository implements AccountRepository {
    private final Map<String, BankAccount> accounts = new ConcurrentHashMap<>();
    private final Map<String, String> cardToAccount = new ConcurrentHashMap<>();
    private final Map<String, String> upiToAccount = new ConcurrentHashMap<>();

    @Override
    public Optional<BankAccount> findByAccountNumber(String accountNumber) {
        return Optional.ofNullable(accounts.get(accountNumber));
    }

    @Override
    public Optional<BankAccount> findByCardNumber(String cardNumber) {
        String acc = cardToAccount.get(cardNumber);
        return acc == null ? Optional.empty() : findByAccountNumber(acc);
    }

    @Override
    public Optional<BankAccount> findByUpiId(String upiId) {
        String acc = upiToAccount.get(upiId);
        return acc == null ? Optional.empty() : findByAccountNumber(acc);
    }

    @Override
    public void save(BankAccount account) {
        accounts.put(account.getAccountNumber(), account);
    }

    // Seeding helpers (a real DB would have these as table rows)
    public void linkCard(String cardNumber, String accountNumber) {
        cardToAccount.put(cardNumber, accountNumber);
    }

    public void linkUpi(String upiId, String accountNumber) {
        upiToAccount.put(upiId, accountNumber);
    }
}