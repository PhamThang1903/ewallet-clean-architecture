package vn.ewallet.wallet.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.ewallet.common.exception.InsufficientBalanceException;
import vn.ewallet.wallet.application.dto.CreateWalletCommand;
import vn.ewallet.wallet.application.dto.MoneyCommand;
import vn.ewallet.wallet.application.dto.WalletBalanceResponse;
import vn.ewallet.wallet.application.dto.WalletResponse;
import vn.ewallet.wallet.domain.model.WalletNumber;
import vn.ewallet.wallet.infrastructure.adapter.out.persistence.entity.WalletJpaEntity;
import vn.ewallet.wallet.infrastructure.adapter.out.persistence.entity.WalletTransactionJpaEntity;
import vn.ewallet.wallet.infrastructure.adapter.out.persistence.repository.WalletJpaRepository;
import vn.ewallet.wallet.infrastructure.adapter.out.persistence.repository.WalletTransactionJpaRepository;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletApplicationService {

    private final WalletJpaRepository walletJpaRepository;
    private final WalletTransactionJpaRepository transactionJpaRepository;

    @Transactional
    public WalletResponse createWallet(CreateWalletCommand command) {
        WalletJpaEntity wallet = new WalletJpaEntity(
                UUID.randomUUID(),
                command.userId(),
                WalletNumber.generate().value(),
                command.currency(),
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                command.walletType(),
                "ACTIVE"
        );

        WalletJpaEntity saved = walletJpaRepository.save(wallet);
        return toResponse(saved);
    }

    @Cacheable(value = "walletBalance", key = "#walletId")
    @Transactional(readOnly = true)
    public WalletBalanceResponse getBalance(UUID walletId) {
        WalletJpaEntity wallet = getWallet(walletId);
        return new WalletBalanceResponse(
                wallet.getId(),
                wallet.getBalance(),
                wallet.getCurrency()
        );
    }

    @Cacheable(value = "walletBalance", key = "#command.walletId()")
    @Transactional
    public WalletBalanceResponse credit(MoneyCommand command) {
        return executeWithOptimisticRetry(() -> {
            if (transactionJpaRepository.existsByIdempotencyKey(command.idempotencyKey())) {
                WalletJpaEntity wallet = getWallet(command.walletId());
                return new WalletBalanceResponse(wallet.getId(), wallet.getBalance(), wallet.getCurrency());
            }

            WalletJpaEntity wallet = getWallet(command.walletId());
            BigDecimal before = wallet.getBalance();
            BigDecimal after = before.add(command.amount());
            wallet.updateBalance(after);
            walletJpaRepository.save(wallet);
            WalletTransactionJpaEntity tx = new WalletTransactionJpaEntity(
                    command.idempotencyKey(),
                    wallet.getId(),
                    "CREDIT",
                    command.category(),
                    command.amount(),
                    before,
                    after,
                    command.referenceId(),
                    command.description()
            );

            transactionJpaRepository.save(tx);
            return new WalletBalanceResponse(wallet.getId(), after, wallet.getCurrency());
        });
    }

    @Cacheable(value = "walletBalance", key = "#command.walletId()")
    @Transactional
    public WalletBalanceResponse debit(MoneyCommand command) {
        return executeWithOptimisticRetry(() -> {
            if (transactionJpaRepository.existsByIdempotencyKey(command.idempotencyKey())) {
                WalletJpaEntity wallet = getWallet(command.walletId());
                return new WalletBalanceResponse(wallet.getId(), wallet.getBalance(), wallet.getCurrency());
            }
            WalletJpaEntity wallet = getWallet(command.walletId());
            BigDecimal before = wallet.getBalance();
            if (before.compareTo(command.amount()) < 0)
                throw new InsufficientBalanceException();

            BigDecimal after = before.subtract(command.amount());
            wallet.updateBalance(after);
            walletJpaRepository.save(wallet);
            WalletTransactionJpaEntity tx = new WalletTransactionJpaEntity(
                    command.idempotencyKey(),
                    wallet.getId(),
                    "DEBIT",
                    command.category(),
                    command.amount(),
                    before,
                    after,
                    command.referenceId(),
                    command.description()
            );

            transactionJpaRepository.save(tx);
            return new WalletBalanceResponse(wallet.getId(), after, wallet.getCurrency());
        });
    }

    @CacheEvict(value = "walletBalance", key = "#walletId")
    @Transactional
    public void freezeWallet(UUID walletId) {
        WalletJpaEntity wallet = getWallet(walletId);
        wallet.freeze();
        walletJpaRepository.save(wallet);
    }

    @Transactional(readOnly = true)
    public Page<WalletTransactionJpaEntity> getTransactionHistory(UUID walletId, Pageable pageable) {
        return transactionJpaRepository.findByWalletIdOrderByCreatedAtDesc(walletId, pageable);
    }

    private <T> T executeWithOptimisticRetry(SupplierWithException<T> supplier) {
        int maxRetries = 3;
        for (int i = 0; i < maxRetries; i++) {
            try {
                return supplier.get();
            } catch (OptimisticLockingFailureException ex) {
                if (i == maxRetries - 1)
                    throw ex;
            }
        }

        throw new IllegalStateException("Optimistic retry failed");
    }

    private WalletJpaEntity getWallet(UUID walletId) {
        return walletJpaRepository.findById(walletId)
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found"));
    }

    private WalletResponse toResponse(WalletJpaEntity wallet) {
        return new WalletResponse(
                wallet.getId(),
                wallet.getUserId(),
                wallet.getWalletNumber(),
                wallet.getBalance(),
                wallet.getCurrency(),
                wallet.getStatus(),
                wallet.getWalletType()
        );
    }

    @FunctionalInterface
    private interface  SupplierWithException<T> {
        T get();
    }
}
