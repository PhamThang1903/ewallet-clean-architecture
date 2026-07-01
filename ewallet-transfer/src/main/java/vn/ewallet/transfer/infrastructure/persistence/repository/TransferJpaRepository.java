package vn.ewallet.transfer.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.ewallet.transfer.infrastructure.persistence.entity.TransferJpaEntity;

import java.util.Optional;
import java.util.UUID;

public interface TransferJpaRepository extends JpaRepository<TransferJpaEntity, UUID> {
    Optional<TransferJpaEntity> findByIdempotencyKey(String idempotencyKey);
}
