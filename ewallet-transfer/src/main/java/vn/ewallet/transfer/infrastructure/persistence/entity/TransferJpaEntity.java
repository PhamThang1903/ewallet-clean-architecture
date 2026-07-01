package vn.ewallet.transfer.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "transfers")
@Getter
public class TransferJpaEntity {

    @Id
    private UUID id;

    @Column(name = "source_wallet_id", nullable = false)
    private UUID sourceWalletId;

    @Column(name = "target_wallet_id", nullable = false)
    private UUID targetWalletId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String currency;

    private String description;

    @Column(name = "idempotency_key", nullable = false, unique = true)
    private String idempotencyKey;

    @Column(nullable = false)
    private String status;

    @Column(name = "current_step", nullable = false)
    private String currentStep;

    @Column(name = "failure_reason")
    private String failureReason;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    protected TransferJpaEntity() {
    }

    public TransferJpaEntity(UUID sourceWalletId,
                             UUID targetWalletId,
                             BigDecimal amount,
                             String currency,
                             String description,
                             String idempotencyKey) {
        this.id = UUID.randomUUID();
        this.sourceWalletId = sourceWalletId;
        this.targetWalletId = targetWalletId;
        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this.idempotencyKey = idempotencyKey;
        this.status = "PENDING";
        this.currentStep = "VALIDATE";
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    public void markValidated() {
        this.status = "VALIDATED";
        this.currentStep = "DEBIT_SOURCE";
        this.updatedAt = OffsetDateTime.now();
    }

    public void markDebited() {
        this.status = "DEBITED";
        this.currentStep = "CREDIT_TARGET";
        this.updatedAt = OffsetDateTime.now();
    }

    public void markCredited() {
        this.status = "CREDITED";
        this.currentStep = "NOTIFY";
        this.updatedAt = OffsetDateTime.now();
    }

    public void markCompleted() {
        this.status = "COMPLETED";
        this.currentStep = "DONE";
        this.updatedAt = OffsetDateTime.now();
    }

    public void markFailed(String reason) {
        this.status = "FAILED";
        this.failureReason = reason;
        this.updatedAt = OffsetDateTime.now();
    }

    public void markCompensated(String reason) {
        this.status = "COMPENSATED";
        this.failureReason = reason;
        this.updatedAt = OffsetDateTime.now();
    }
}