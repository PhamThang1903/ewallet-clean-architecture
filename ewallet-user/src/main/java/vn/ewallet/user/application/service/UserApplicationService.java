package vn.ewallet.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.ewallet.user.infrastructure.adapter.in.web.dto.CreateUserRequest;
import vn.ewallet.user.infrastructure.adapter.in.web.dto.UserResponse;
import vn.ewallet.user.infrastructure.adapter.out.persistence.entity.UserJpaEntity;
import vn.ewallet.user.infrastructure.adapter.out.persistence.repository.UserJpaRepository;
import vn.ewallet.user.infrastructure.security.SimpleCryptoService;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserApplicationService {
    private final UserJpaRepository userJpaRepository;
    private final SimpleCryptoService cryptoService;

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        String phoneHash = cryptoService.sha256(request.phoneNumber());
        String emailHash = cryptoService.sha256(request.email());

        if (userJpaRepository.existsByPhoneHash(phoneHash))
            throw new IllegalArgumentException("Phone number already exists");

        if (userJpaRepository.existsByEmailHash(emailHash))
            throw new IllegalArgumentException("Email already exists");

        UserJpaEntity entity = new UserJpaEntity(
                UUID.randomUUID(),
                request.keycloakUserId(),
                cryptoService.encrypt(request.phoneNumber()),
                cryptoService.encrypt(request.email()),
                phoneHash,
                emailHash,
                "ACTIVE",
                "LO",
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );

        UserJpaEntity saved = userJpaRepository.save(entity);
        return toResponse(saved);
    }

    @Transactional
    public UserResponse getByKeycloakUserId(String keycloakUserId) {
        UserJpaEntity user = userJpaRepository.findByKeycloakUserId(keycloakUserId)
                .orElseThrow(()-> new IllegalArgumentException("User not found"));
        return toResponse(user);
    }

    @Transactional
    public UserResponse getById(UUID userId) {
        UserJpaEntity user = userJpaRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return toResponse(user);
    }

    @Transactional
    public void deleteByKeycloakUserId(String keycloakUserId) {
        UserJpaEntity user = userJpaRepository.findByKeycloakUserId(keycloakUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.markDeleted();
        userJpaRepository.save(user);
    }

    private UserResponse toResponse(UserJpaEntity user) {
        return new UserResponse(
                user.getId(),
                user.getKeycloakUserId(),
                cryptoService.decrypt(user.getPhoneNumberEncrypted()),
                cryptoService.decrypt(user.getEmailEncrypted()),
                user.getStatus(),
                user.getKycLevel()
        );
    }
}
