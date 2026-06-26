package vn.ewallet.user.infrastructure.adapter.in.web.dto;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String keycloakUserId,
        String phoneNumber,
        String email,
        String status,
        String kycLevel
) {
}
