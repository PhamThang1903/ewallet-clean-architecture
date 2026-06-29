package vn.ewallet.common.exception;

public class InsufficientBalanceException extends BusinessException {
    public InsufficientBalanceException() {
        super(ErrorCode.INSUFFICIENT_BALANCE, "Insufficient Balance");
    }
}
