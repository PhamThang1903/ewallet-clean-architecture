package vn.ewallet.transfer.domain.model;

public enum TransferStep {
    VALIDATE,
    DEBIT_SOURCE,
    CREDIT_TARGET,
    NOTIFY,
    AUDIT,
    REWARD,
    DONE
}
