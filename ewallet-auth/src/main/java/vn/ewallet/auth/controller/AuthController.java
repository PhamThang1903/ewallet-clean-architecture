package vn.ewallet.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.ewallet.auth.client.KeycloakAuthClient;
import vn.ewallet.auth.dto.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final KeycloakAuthClient keycloakAuthClient;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(Map.of(
                "message", "Register API created. Keycloak admin integration will be added next.",
                "username", request.username()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(keycloakAuthClient.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(keycloakAuthClient.refresh(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(Map.of(
                "message", "Logout API placeholder"
        ));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword() {
        return ResponseEntity.ok(Map.of(
                "message", "Forgot password API placeholder"
        ));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp() {
        return ResponseEntity.ok(Map.of(
                "message", "Verify OTP API placeholder"
        ));
    }

    @PostMapping("/step-up/check")
    public ResponseEntity<?> checkStepUp(@RequestBody StepUpAuthRequest request) {
        boolean highValue = request.amount() != null && request.amount().compareTo(new BigDecimal("5000000")) > 0;
        boolean required = highValue;
        return ResponseEntity.ok(Map.of(
                "stepUpRequired", required,
                "reason", required ? "HIGH_VALUE_TRANSACTION" : "NONE"
        ));
    }

    @GetMapping("/devices")
    public ResponseEntity<?> getTrustedDevices() {
        return ResponseEntity.ok(List.of(Map.of(
                "deviceId", "android-samsung-s918n-abc123",
                "deviceName", "Samsung S23 Ultra",
                "trusted", true
        )));
    }
}
