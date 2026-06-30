package vn.ewallet.wallet.infrastructure.adapter.in.web.dto;

public record CreateWalletRequest(
        String currency,
        String walletType
) {
}
