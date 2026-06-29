package vn.ewallet.common.outbox;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, UUID> {
    List<OutboxEvent> findTop100ByStatusAndNextRetryAtBeforeOrderByCreatedAtAsc(
            OutboxStatus status,
            OffsetDateTime now
    );
}
