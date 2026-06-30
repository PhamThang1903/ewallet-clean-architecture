package vn.ewallet.wallet.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletResponse(
        UUID walletId,
        UUID userId,
        String walletNumber,
        BigDecimal balance,
        String currency,
        String status,
        String walletType
) {
}
