package vn.ewallet.wallet.domain.model;

import java.security.SecureRandom;

public record WalletNumber(String value) {

    private static final SecureRandom RANDOM = new SecureRandom();

    public WalletNumber {
        if (value == null || value.matches("^EW[0-9]{10}$"))
            throw new IllegalArgumentException("Invalid wallet number");
    }

    public static WalletNumber generate() {
        long number = 1_000_000_000L + Math.abs(RANDOM.nextLong() % 9_000_000_000L);
        return new WalletNumber("EW" + number);
    }
}
