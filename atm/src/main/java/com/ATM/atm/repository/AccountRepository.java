package com.ATM.atm.repository;

import com.ATM.atm.entity.BankAccount;
import java.util.Optional;

public interface AccountRepository {
    Optional<BankAccount> findByAccountNumber(String accountNumber);
    Optional<BankAccount> findByCardNumber(String cardNumber);
    Optional<BankAccount> findByUpiId(String upiId);
    void save(BankAccount account);
}