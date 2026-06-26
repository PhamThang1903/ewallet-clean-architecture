package vn.ewallet.user.infrastructure.adapter.in.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(@RequestHeader("X-User-Id") String keycloakUserId) {
        return ResponseEntity.ok(userService.getByKeycloakUserId(keycloakUserId));
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateMe(@RequestBody UpdateProfileRequest request, @RequestHeader("X-User-Id") String keycloakUserId) {
        return ResponseEntity.ok(Map.of(
                "message", "Update profile placeholder",
                "keycloakUserId", keycloakUserId
        ));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getById(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getById(userId));
    }

    @PutMapping("/me/avatar")
    public ResponseEntity<?> updateAvatar(@RequestBody Map<String, String> request, @RequestHeader("X-User-Id") String keycloakUserId) {
        return ResponseEntity.ok(Map.of(
                "message", "Update avatar placeholder",
                "avatarUrl", request.get("avatarUrl")
        ));
    }

    @DeleteMapping("/me")
    public ResponseEntity<?> deleteMe(@RequestHeader("X-User-Id") String keycloakUserId) {
        userService.deleteByKeycloakUserId(keycloakUserId);
        return ResponseEntity.ok(Map.of(
                "message", "User deleted"
        ));
    }

    @PostMapping("/me/otp/send")
    public ResponseEntity<?> sendOtp(@RequestHeader("X-User-Id") String keycloakUserId, @RequestBody SendOtpRequest request) {
        String otp = otpService.generateOtp(keycloakUserId, request.purpose());
        return ResponseEntity.ok(Map.of(
                "message", "OTP generated",
                "devOtp", otp
        ));
    }

    @PostMapping("/me/otp/verify")
    public ResponseEntity<?> verifyOtp(@RequestHeader("X-User-Id") String keycloakUserId, @RequestBody VerifyOtpRequest request) {
        boolean valid = otpService.verifyOtp(keycloakUserId, request.purpose(), request.otp());
        if (valid)
            userService.markVerified(keycloakUserId, request.purpose());
        if (!valid) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", "Invalid or expired OTP"
            ));
        }

        return ResponseEntity.ok(Map.of(
                "message", "OTP verified",
                "purpose", request.purpose()
        ));
    }
}
