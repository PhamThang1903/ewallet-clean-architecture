package vn.ewallet.wallet.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.ewallet.wallet.infrastructure.adapter.out.persistence.entity.WalletTransactionJpaEntity;

import java.util.UUID;

public interface WalletTransactionJpaRepository extends JpaRepository<WalletTransactionJpaEntity, UUID> {
    Page<WalletTransactionJpaEntity> findByWalletIdOrderByCreatedAtDesc(UUID walletId, Pageable pageable);
    boolean existsByIdempotencyKey(String idempotencyKey);
}
