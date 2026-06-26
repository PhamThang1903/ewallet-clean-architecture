package vn.ewallet.user.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
public class UserJpaEntity {

    @Id
    private UUID id;

    @Column(name = "keycloak_user_id", nullable = false, unique = true)
    private String keycloakUserId;

    @Column(name = "phone_number_encrypted", nullable = false)
    private String phoneNumberEncrypted;

    @Column(name = "email_encrypted", nullable = false)
    private String emailEncrypted;

    @Column(name = "phone_hash", nullable = false, unique = true)
    private String phoneHash;

    @Column(name = "email_hash", nullable = false, unique = true)
    private String emailHash;

    @Column(nullable = false)
    private String status;

    @Column(name = "kyc_level", nullable = false)
    private String kycLevel;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    protected UserJpaEntity() {
        
    }
}
