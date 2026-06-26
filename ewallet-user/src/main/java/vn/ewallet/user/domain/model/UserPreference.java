package vn.ewallet.user.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserPreference {
    private UUID id;
    private UUID userId;
    private String language;
    private boolean pushEnabled;
    private boolean emailEnabled;
    private boolean smsEnabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserPreference(UUID userId) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.language = "vi";
        this.pushEnabled = true;
        this.emailEnabled = true;
        this.smsEnabled = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateNotificationSetting(boolean pushEnabled, boolean emailEnabled, boolean smsEnabled) {
        this.pushEnabled = pushEnabled;
        this.emailEnabled = emailEnabled;
        this.smsEnabled = smsEnabled;
        this.updatedAt = LocalDateTime.now();
    }

    public void changeLanguage(String language) {
        this.language = language;
        this.updatedAt = LocalDateTime.now();
    }
}
