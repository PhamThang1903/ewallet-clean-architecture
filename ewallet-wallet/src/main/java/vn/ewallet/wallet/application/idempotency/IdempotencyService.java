package vn.ewallet.wallet.application.idempotency;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class IdempotencyService {
    private static final Duration TTL = Duration.ofHours(24);

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public <T> T execute(String idempotencyKey, Class<T> responseType, Supplier<T> supplier) {
        String redisKey = buildKey(idempotencyKey);
        String cached = redisTemplate.opsForValue().get(redisKey);
        if (cached != null) {
            try {
                return objectMapper.readValue(cached, responseType);
            } catch (Exception e) {
                throw new IllegalStateException("Cannot deserialize idempotency response", e);
            }
        }

        T response = supplier.get();
        try {
            String json = objectMapper.writeValueAsString(response);
            redisTemplate.opsForValue().set(redisKey, json, TTL);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot serialize idempotency response", e);
        }
        return response;
    }

    private String buildKey(String idempotencyKey) {
        return "idempotency:wallet:" + idempotencyKey;
    }
}
