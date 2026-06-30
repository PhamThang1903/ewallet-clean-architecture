package vn.ewallet.wallet.application.dto;

import java.util.UUID;

public record CreateWalletCommand(
        UUID userId,
        String currency,
        String walletType
) {
}
