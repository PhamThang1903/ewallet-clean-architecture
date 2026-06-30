package vn.ewallet.wallet.domain.model;

import lombok.Getter;
import vn.ewallet.common.domain.Money;
import vn.ewallet.common.exception.InsufficientBalanceException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.UUID;

@Getter
public class Wallet {
    private UUID id;
    private UUID userId;
    private WalletNumber walletNumber;
    private Currency currency;
    private Money balance;
    private Money frozenBalance;
    private WalletStatus status;
    private WalletType type;
    private Long version;
    private LocalDateTime createdAt;

    public Wallet(UUID id, UUID userId, Currency currency, WalletType type) {
        this.id = id;
        this.userId = userId;
        this.currency = currency;
        this.walletNumber = WalletNumber.generate();
        this.balance = new Money(BigDecimal.ZERO, currency);
        this.status = WalletStatus.ACTIVE;
        this.type = type;
        this.version = 0L;
        this.createdAt = LocalDateTime.now();
    }

    public void credit(Money amount) {
        ensureActive();
        this.balance.add(amount);
    }

    public void debit(Money amount) {
        ensureActive();
        if (this.balance.isLessThan(amount))
            throw new InsufficientBalanceException();
        this.balance.subtract(amount);
    }

    public void freeze() {
        if (this.status == WalletStatus.CLOSED)
            throw new IllegalStateException("Cannot freeze closed wallet");
        this.status = WalletStatus.FROZEN;
    }

    public void close() {
        if (this.balance.amount().compareTo(BigDecimal.ZERO) > 0)
            throw new IllegalStateException("Cannot close wallet with positive balance");
        this.status = WalletStatus.CLOSED;
    }

    private void ensureActive() {
        if (this.status != WalletStatus.ACTIVE)
            throw new IllegalStateException("Wallet is not active");
    }
}
