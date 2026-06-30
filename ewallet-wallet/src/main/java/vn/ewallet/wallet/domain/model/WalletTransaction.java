package vn.ewallet.wallet.domain.model;

import vn.ewallet.common.domain.Money;

import java.time.LocalDateTime;
import java.util.UUID;

public class WalletTransaction {
    private UUID id;
    private String idempotencyKey;
    private UUID walletId;
    private TransactionType type;
    private TransactionCategory category;
    private Money amount;
    private Money balanceBefore;
    private Money balanceAfter;
    private UUID referenceId;
    private String description;
    private String status;
    private LocalDateTime createdAt;

    public WalletTransaction(
            String idempotencyKey,
            UUID walletId,
            TransactionType type,
            TransactionCategory category,
            Money amount,
            Money balanceBefore,
            Money balanceAfter,
            UUID referenceId,
            String description
    ) {
        this.id = UUID.randomUUID();
        this.idempotencyKey = idempotencyKey;
        this.walletId = walletId;
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.balanceBefore = balanceBefore;
        this.referenceId = referenceId;
        this.description = description;
        this.status = "COMPLETE";
        this.createdAt = LocalDateTime.now();
    }
}
