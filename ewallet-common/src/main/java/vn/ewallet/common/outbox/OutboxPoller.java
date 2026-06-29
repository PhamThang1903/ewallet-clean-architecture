package vn.ewallet.common.outbox;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class OutboxPoller {

    private final OutboxEventRepository repository;
    private final OutboxEventPublisher publisher;

    @Scheduled(fixedDelayString = "${outbox.poller.fixed-delay-ms:5000}")
    @Transactional
    public void pollAndPublishe() {
        List<OutboxEvent> events = repository.findTop100ByStatusAndNextRetryAtBeforeOrderByCreatedAtAsc(
                OutboxStatus.PENDING,
                OffsetDateTime.now()
        );
        events.forEach(event -> {
            try {
                publisher.publish(event);
                event.markPublished();
                repository.save(event);
                log.info("Outbox event published id={} type={}", event.getId(), event.getEventType());
            } catch (Exception e) {
                event.markFailedForRetry();
                repository.save(event);
                log.error("Failed to publish outbox event id={} retryCount={}", event.getId(), event.getRetryCount(), e);
            }
        });
    }

}
