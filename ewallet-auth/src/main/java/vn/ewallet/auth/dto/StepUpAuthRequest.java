package vn.ewallet.auth.dto;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record StepUpAuthRequest(
        @NotBlank String userId,
        @NotBlank String deviceId,
        BigDecimal amount,
        @NotBlank String action
) {
}
