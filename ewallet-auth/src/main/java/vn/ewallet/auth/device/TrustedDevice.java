package vn.ewallet.auth.device;

import java.time.LocalDateTime;
import java.util.UUID;

public class TrustedDevice {
    private UUID id;
    private UUID userId;
    private String deviceId;
    private String deviceName;
    private String platform;
    private boolean trusted;
    private LocalDateTime firstSeenAt;
    private LocalDateTime lastSeenAt;

    public TrustedDevice(UUID userId, String deviceId, String deviceName, String platform) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.platform = platform;
        this.trusted = false;
        this.firstSeenAt = LocalDateTime.now();
        this.lastSeenAt = LocalDateTime.now();
    }

    public void markTrust() {
        this.trusted = true;
    }

    public boolean isTrusted() {
        return this.trusted;
    }
}
