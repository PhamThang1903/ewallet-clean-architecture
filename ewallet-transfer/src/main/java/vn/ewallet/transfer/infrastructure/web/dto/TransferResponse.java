package vn.ewallet.transfer.infrastructure.web.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferResponse(
        UUID transferId,
        UUID sourceWalletId,
        UUID targetWalletId,
        BigDecimal amount,
        String currency,
        String status,
        String description
) {
}
