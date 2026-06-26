package vn.ewallet.user.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(
        @NotBlank String keycloakUserId,
        @NotBlank String phoneNumber,
        @Email String email,
        String fullName
) {
}
