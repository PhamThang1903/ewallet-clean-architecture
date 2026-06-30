package vn.ewallet.wallet.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "wallet_transactions")
@IdClass(WalletTransactionId.class)
@NoArgsConstructor
public class WalletTransactionJpaEntity {

    @Id
    private UUID id;

    @Id
    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "idempotency_key", nullable = false, unique = true)
    private String idempotencyKey;

    @Column(name = "wallet_id", nullable = false)
    private UUID walletId;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "balance_before", nullable = false)
    private BigDecimal balanceBefore;

    @Column(name = "balance_after", nullable = false)
    private BigDecimal balanceAfter;

    @Column(name = "reference_id")
    private UUID referenceId;

    private String description;

    private String status;

    public WalletTransactionJpaEntity(
            String idempotencyKey,
            UUID walletId,
            String type,
            String category,
            BigDecimal amount,
            BigDecimal balanceBefore,
            BigDecimal balanceAfter,
            UUID referenceId,
            String description
    ) {
        this.id = UUID.randomUUID();
        this.createdAt = OffsetDateTime.now();
        this.idempotencyKey = idempotencyKey;
        this.walletId = walletId;
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceAfter;
        this.referenceId = referenceId;
        this.description = description;
        this.status = "COMPLETED";
    }
}
