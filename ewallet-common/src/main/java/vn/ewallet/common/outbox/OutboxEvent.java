package vn.ewallet.common.outbox;

import jakarta.persistence.*;
import lombok.Getter;
import vn.ewallet.common.entity.AuditableEntity;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "outbox_events")
@Getter
public class OutboxEvent extends AuditableEntity {
    @Id
    private UUID id;

    @Column(name = "aggregate_id", nullable = false)
    private UUID aggregateId;

    @Column(name = "aggregate_type", nullable = false, length = 100)
    private String aggregateType;

    @Column(name = "event_type", nullable = false, length = 100)
    private String eventType;

    @Column(name = "payload", nullable = false, columnDefinition = "TEXT")
    private String payload;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private OutboxStatus status;

    @Column(name = "published_at")
    private OffsetDateTime publishedAt;

    @Column(name = "retry_count", nullable = false)
    private int retryCount;
    @Column(name = "next_retry_at")
    private OffsetDateTime nextRetryAt;

    protected OutboxEvent() {}

    public OutboxEvent(UUID aggregateId,
                       String aggregateType,
                       String eventType,
                       String payload) {
        this.id = UUID.randomUUID();
        this.aggregateId = aggregateId;
        this.aggregateType = aggregateType;
        this.eventType = eventType;
        this.payload = payload;
        this.status = OutboxStatus.PENDING;
        this.retryCount = 0;
        this.nextRetryAt = OffsetDateTime.now();
    }

    public void markPublished() {
        this.status = OutboxStatus.PUBLISHED;
        this.publishedAt = OffsetDateTime.now();
    }

    public void markFailedForRetry() {
        this.retryCount++;

        if (this.retryCount >= 5){
            this.status = OutboxStatus.FAILED;
            return;
        }

        long delaySeconds = (long) Math.pow(2, this.retryCount) * 10;
        this.nextRetryAt = OffsetDateTime.now().plusSeconds(delaySeconds);
    }
}
