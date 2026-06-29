package vn.ewallet.common.outbox;

public interface OutboxEventPublisher {
    void publish(OutboxEvent event);
}
