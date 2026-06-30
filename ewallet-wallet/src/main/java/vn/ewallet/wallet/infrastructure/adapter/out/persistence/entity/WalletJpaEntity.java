package vn.ewallet.wallet.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "wallets")
@Getter
public class WalletJpaEntity {
    @Id
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "wallet_number", nullable = false, updatable = true)
    private String walletNumber;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(name = "frozen_balance", nullable = false)
    private BigDecimal frozenBalance;

    @Column(name = "wallet_type", nullable = false)
    private String walletType;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    @Version
    private Long version = 0L;

    @Column(name = "created_at")
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    public WalletJpaEntity(
            UUID id,
            UUID userId,
            String walletNumber,
            String currency,
            BigDecimal balance,
            BigDecimal frozenBalance,
            String walletType,
            String status
    ) {
        this.id = id;
        this.userId = userId;
        this.walletNumber = walletNumber;
        this.currency = currency;
        this.balance = balance;
        this.frozenBalance = frozenBalance;
        this.walletType = walletType;
        this.status = status;
        this.version = 0L;
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    public void updateBalance(BigDecimal balance) {
        this.balance = balance;
        this.updatedAt = OffsetDateTime.now();
    }

    public void freeze() {
        this.status = "FROZEN";
        this.updatedAt = OffsetDateTime.now();
    }
}
