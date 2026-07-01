package vn.ewallet.transfer.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.ewallet.transfer.infrastructure.persistence.entity.TransferJpaEntity;
import vn.ewallet.transfer.infrastructure.persistence.repository.TransferJpaRepository;
import vn.ewallet.transfer.infrastructure.web.dto.CreateTransferRequest;
import vn.ewallet.transfer.infrastructure.web.dto.TransferResponse;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransferApplicationService {
    private final TransferJpaRepository transferJpaRepository;
    private final TransferSagaOrchestrator transferSagaOrchestrator;

    @Transactional
    public TransferResponse createTransfer(CreateTransferRequest request, String idempotencyKey) {
        var existing = transferJpaRepository.findByIdempotencyKey(idempotencyKey);
        if (existing.isPresent())
            return toResponse(existing.get());
        TransferJpaEntity transferJpa = new TransferJpaEntity(
                request.sourceWalletId(),
                request.targetWalletId(),
                request.amount(),
                request.currency() == null ? "VND" : request.currency(),
                request.description(),
                idempotencyKey
        );

        transferJpaRepository.save(transferJpa);
        TransferJpaEntity completed = transferSagaOrchestrator.execute(transferJpa);
        return toResponse(completed);
    }

    @Transactional(readOnly = true)
    public TransferResponse getTransfer(UUID transferId) {
        TransferJpaEntity transfer = transferJpaRepository.findById(transferId)
                .orElseThrow(() -> new IllegalArgumentException("Transfer not found"));
        return toResponse(transfer);
    }

    @Transactional(readOnly = true)
    public Page<TransferResponse> getTransferByWallet(UUID walletId, Pageable pageable) {
        return transferJpaRepository.findBySourceWalletIdOrTargetWalletIdOrderByCreatedAtDesc(walletId, walletId, pageable).map(this::toResponse);
    }

    @Transactional
    public TransferResponse cancelTransfer(UUID transferId) {
        TransferJpaEntity transfer = transferJpaRepository.findById(transferId)
                .orElseThrow(() -> new IllegalArgumentException("Transfer not found"));
        if (!"PENDING".equals(transfer.getStatus()))
            throw new IllegalStateException("Only PENDING transfer can be cancelled");

        transfer.markFailed("Cancelled by user");
        return toResponse(transferJpaRepository.save(transfer));
    }

    private TransferResponse toResponse(TransferJpaEntity transferJpa) {
        return new TransferResponse(
                transferJpa.getId(),
                transferJpa.getSourceWalletId(),
                transferJpa.getTargetWalletId(),
                transferJpa.getAmount(),
                transferJpa.getCurrency(),
                transferJpa.getStatus(),
                transferJpa.getDescription()
        );
    }
}
