package vn.ewallet.wallet.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.ewallet.wallet.infrastructure.adapter.out.persistence.entity.WalletJpaEntity;

import java.util.Optional;
import java.util.UUID;

public interface WalletJpaRepository extends JpaRepository<WalletJpaEntity, UUID> {
    Optional<WalletJpaEntity> findByUserIdAndWalletType(UUID userId, String walletType);
}
