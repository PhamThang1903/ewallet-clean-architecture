package vn.ewallet.user.infrastructure.adapter.in.web.dto;

import java.time.LocalDate;

public record UpdateProfileRequest(
        String fullName,
        LocalDate dateOfBirth,
        String gender,
        String avatarUrl
) {
}
