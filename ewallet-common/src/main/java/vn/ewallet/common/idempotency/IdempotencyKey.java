package vn.ewallet.common.idempotency;

import java.util.UUID;
import java.util.regex.Pattern;

public record IdempotencyKey(String value) {

    private static final Pattern PATTERN = Pattern.compile("^[a-zA-Z0-9\\-_:]{8,128}$");

    public IdempotencyKey {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Idempotency key is required");
        }
        if (!PATTERN.matcher(value).matches())
            throw new IllegalArgumentException("Invalid idempotency key format");
    }

    public static IdempotencyKey random() {
        return new IdempotencyKey(UUID.randomUUID().toString());
    }
}
