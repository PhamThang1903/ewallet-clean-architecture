package vn.ewallet.user.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class User {
    private UUID id;
    private String keycloakUserId;
    private String phoneNumber;
    private String email;
    private UserStatus status;
    private KycLevel kycLevel;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User(UUID id, String keycloakUserId, String phoneNumber, String email) {
        this.id = id;
        this.keycloakUserId = keycloakUserId;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.status = UserStatus.ACTIVE;
        this.kycLevel = KycLevel.L0;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void suspend() {
        if (this.status == UserStatus.DELETED)
            throw new IllegalStateException("Cannot suspend deleted user");
        this.status = UserStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status == UserStatus.DELETED)
            throw new IllegalStateException("Cannot active deleted user");
        this.status = UserStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateKycLevel(KycLevel kycLevel) {
        this.kycLevel = kycLevel;
        this.updatedAt = LocalDateTime.now();
    }
}
