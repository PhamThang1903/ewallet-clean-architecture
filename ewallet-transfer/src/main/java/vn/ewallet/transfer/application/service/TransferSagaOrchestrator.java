package vn.ewallet.transfer.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.ewallet.transfer.infrastructure.client.WalletServiceClient;
import vn.ewallet.transfer.infrastructure.persistence.entity.TransferJpaEntity;
import vn.ewallet.transfer.infrastructure.persistence.repository.TransferJpaRepository;

@Service
@RequiredArgsConstructor
public class TransferSagaOrchestrator {
    private final TransferJpaRepository transferJpaRepository;
    private final WalletServiceClient walletServiceClient;

    @Transactional
    public TransferJpaEntity execute(TransferJpaEntity transfer) {
        try {
            validate(transfer);
            transfer.markValidated();
            transferJpaRepository.save(transfer);

            debitSourceWallet(transfer);
            transfer.markDebited();
            transferJpaRepository.save(transfer);

            creditTargetWallet(transfer);
            transfer.markCredited();
            transferJpaRepository.save(transfer);

            notifyBestEffort(transfer);
            auditBestEffort(transfer);
            rewardBestEffort(transfer);

            transfer.markCompleted();
            return transferJpaRepository.save(transfer);
        } catch (Exception ex) {
            compensateIfNeeded(transfer);
            transfer.markFailed(ex.getMessage());
            return transferJpaRepository.save(transfer);
        }
    }

    private void validate(TransferJpaEntity transfer) {
        if (transfer.getSourceWalletId().equals(transfer.getTargetWalletId()))
            throw new IllegalArgumentException("Cannot transfer to same wallet");

        if (transfer.getAmount().signum() <= 0)
            throw new IllegalArgumentException("Amount must be positive");
    }

    private void debitSourceWallet(TransferJpaEntity transfer) {
        walletServiceClient.debit(transfer.getSourceWalletId(),
                transfer.getIdempotencyKey() + ":debit", new WalletServiceClient.WalletMoneyRequest(
                        transfer.getAmount(),
                        transfer.getCurrency(),
                        "TRANSFER",
                        transfer.getId(),
                        transfer.getDescription()
                ));
    }

    private void creditTargetWallet(TransferJpaEntity transfer) {
        walletServiceClient.credit(
                transfer.getTargetWalletId(),
                transfer.getIdempotencyKey() + ":credit",
                new WalletServiceClient.WalletMoneyRequest(
                        transfer.getAmount(),
                        transfer.getCurrency(),
                        "TRANSFER",
                        transfer.getId(),
                        transfer.getDescription()
                )
        );
    }

    private void compensateIfNeeded(TransferJpaEntity  transfer) {
        if ("DEBITED".equals(transfer.getStatus())) {
            walletServiceClient.credit(
                    transfer.getSourceWalletId(),
                    transfer.getIdempotencyKey() + ":compensate-credit-source",
                    new WalletServiceClient.WalletMoneyRequest(
                            transfer.getAmount(),
                            transfer.getCurrency(),
                            "REFUND",
                            transfer.getId(),
                            "Compensation for failed transfer"
                    )
            );
        }
        transfer.markCompensated("Compensated source wallet");
    }

    private void notifyBestEffort(TransferJpaEntity transfer) {

    }

    private void auditBestEffort(TransferJpaEntity transferJpa) {

    }

    private void rewardBestEffort(TransferJpaEntity transferJpa) {}
}
