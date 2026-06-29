package vn.ewallet.user.infrastructure.adapter.in.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.ewallet.common.response.ApiResponse;
import vn.ewallet.user.application.service.OtpService;
import vn.ewallet.user.application.service.UserApplicationService;
import vn.ewallet.user.infrastructure.adapter.in.web.dto.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserApplicationService userService;
    private final OtpService otpService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(ApiResponse.success(userService.createUser(request)));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMe(@RequestHeader("X-User-Id") String keycloakUserId) {
        return ResponseEntity.ok(ApiResponse.success(userService.getByKeycloakUserId(keycloakUserId)));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<?>> updateMe(@RequestBody UpdateProfileRequest request, @RequestHeader("X-User-Id") String keycloakUserId) {
        return ResponseEntity.ok(ApiResponse.success(Map.of(
                "message", "Update profile placeholder",
                "keycloakUserId", keycloakUserId
        )));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> getById(@PathVariable UUID userId) {
        return ResponseEntity.ok(ApiResponse.success(userService.getById(userId)));
    }

    @PutMapping("/me/avatar")
    public ResponseEntity<ApiResponse<?>> updateAvatar(@RequestBody Map<String, String> request, @RequestHeader("X-User-Id") String keycloakUserId) {
        return ResponseEntity.ok(ApiResponse.success(Map.of(
                "message", "Update avatar placeholder",
                "avatarUrl", request.get("avatarUrl")
        )));
    }

    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<?>> deleteMe(@RequestHeader("X-User-Id") String keycloakUserId) {
        userService.deleteByKeycloakUserId(keycloakUserId);
        return ResponseEntity.ok(ApiResponse.success(Map.of(
                "message", "User deleted"
        )));
    }

    @PostMapping("/me/otp/send")
    public ResponseEntity<ApiResponse<?>> sendOtp(@RequestHeader("X-User-Id") String keycloakUserId, @RequestBody SendOtpRequest request) {
        String otp = otpService.generateOtp(keycloakUserId, request.purpose());
        return ResponseEntity.ok(ApiResponse.success(Map.of(
                "message", "OTP generated",
                "devOtp", otp
        )));
    }

    @PostMapping("/me/otp/verify")
    public ResponseEntity<ApiResponse<?>> verifyOtp(@RequestHeader("X-User-Id") String keycloakUserId, @RequestBody VerifyOtpRequest request) {
        boolean valid = otpService.verifyOtp(keycloakUserId, request.purpose(), request.otp());
        if (valid)
            userService.markVerified(keycloakUserId, request.purpose());
        if (!valid) {
            return ResponseEntity.badRequest().body(ApiResponse.success(Map.of(
                    "message", "Invalid or expired OTP"
            )));
        }

        return ResponseEntity.ok(ApiResponse.success(Map.of(
                "message", "OTP verified",
                "purpose", request.purpose()
        )));
    }
}
