package vn.ewallet.wallet.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletBalanceResponse(
        UUID walletId,
        BigDecimal balance,
        String currency
) {
}
