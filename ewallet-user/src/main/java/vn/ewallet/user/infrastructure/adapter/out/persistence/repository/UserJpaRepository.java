package vn.ewallet.user.infrastructure.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.ewallet.user.infrastructure.adapter.out.persistence.entity.UserJpaEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {
    Optional<UserJpaEntity> findByKeycloakUserId(String keycloakUserId);
    boolean existsByPhoneHash(String phoneHash);
    boolean existsByEmailHash(String emailHash);
}
