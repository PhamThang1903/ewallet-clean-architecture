package vn.ewallet.user.infrastructure.adapter.in.web.dto;

public record VerifyOtpRequest(
        String purpose,
        String otp
) {
}
