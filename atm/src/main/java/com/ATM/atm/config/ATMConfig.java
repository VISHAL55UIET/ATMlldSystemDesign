package com.ATM.atm.config;

public final class ATMConfig {
    private ATMConfig() {}

    public static final int MAX_PIN_ATTEMPTS = 3;
    public static final long PER_TXN_LIMIT = 20_000;
    public static final long DAILY_WITHDRAWAL_LIMIT = 40_000;
    public static final int NOTE_MULTIPLE = 100;
}