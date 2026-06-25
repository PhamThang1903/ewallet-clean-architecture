package vn.ewallet.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank String username,
        @Email String email,
        @NotBlank String phoneNumber,
        @Size(min = 6) String password
) {
}
