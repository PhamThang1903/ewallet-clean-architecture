package vn.ewallet.wallet.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record MoneyCommand(
        UUID walletId,
        BigDecimal amount,
        String currency,
        String category,
        UUID referenceId,
        String idempotencyKey,
        String description
) {
}
