package vn.ewallet.wallet.infrastructure.adapter.in.web.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record MoneyRequest(
        BigDecimal amount,
        String currency,
        String category,
        UUID referenceId,
        String description
) {
}
