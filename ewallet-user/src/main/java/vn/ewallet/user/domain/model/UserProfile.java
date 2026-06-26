package vn.ewallet.user.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class UserProfile {
    private UUID id;
    private UUID userId;
    private String fullName;
    private LocalDate dateOfBirth;
    private String gender;
    private String avatarUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserProfile(UUID userId, String fullName) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.fullName = fullName;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateProfile(String fullName, LocalDate dateOfBirth, String gender, String avatarUrl) {
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.avatarUrl = avatarUrl;
        this.updatedAt = LocalDateTime.now();
    }
}
