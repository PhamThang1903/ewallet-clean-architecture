package vn.ewallet.gateway.fallback;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
public class FallbackController {

    @GetMapping("/fallback/user")
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public Map<String, Object> userServiceFallback() {
        return Map.of(
          "success", false,
          "message", "User Service is temporarily unavailable",
          "timestamp", Instant.now().toString()
        );
    }

    @GetMapping("/fallback/wallet")
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public Map<String, Object> walletServiceFallback() {
        return Map.of(
                "success", false,
                "message", "Wallet Service is temporarily unavailable",
                "timestamp", Instant.now().toString()
        );
    }
}
