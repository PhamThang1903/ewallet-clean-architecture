package vn.ewallet.transfer.infrastructure.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateTransferRequest(
        @NotNull UUID sourceWalletId,
        @NotNull UUID targetWalletId,
        @NotNull @DecimalMin("1.00")BigDecimal amount,
        String currency,
        String description
) {
}
