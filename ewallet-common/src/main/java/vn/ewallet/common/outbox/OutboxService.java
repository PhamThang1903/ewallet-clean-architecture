package vn.ewallet.common.outbox;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OutboxService {
    private final OutboxEventRepository repository;

    @Transactional
    public void saveEvent(UUID aggregateId,
                          String aggregateType,
                          String eventType,
                          String payload){
        OutboxEvent event = new OutboxEvent(
                aggregateId,
                aggregateType,
                eventType,
                payload
        );

        repository.save(event);
    }
}
