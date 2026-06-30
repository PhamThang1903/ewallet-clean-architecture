package vn.ewallet.wallet.application.idempotency;

public record IdempotencyRecord(
        String key,
        String responseJson
) {
}
