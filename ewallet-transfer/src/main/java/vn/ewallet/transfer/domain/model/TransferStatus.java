package vn.ewallet.transfer.domain.model;

public enum TransferStatus {
    PENDING,
    VALIDATED,
    DEBITED,
    CREDITED,
    COMPLETED,
    FAILED,
    COMPENSATED
}
