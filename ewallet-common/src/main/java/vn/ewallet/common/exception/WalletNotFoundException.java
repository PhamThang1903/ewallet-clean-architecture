package vn.ewallet.common.exception;

public class WalletNotFoundException extends BusinessException {

    public WalletNotFoundException(String walletId) {
        super(ErrorCode.WALLET_NOT_FOUND, "Wallet not found: " + walletId);
    }
}
