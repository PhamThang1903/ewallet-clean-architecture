package vn.ewallet.transfer.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.math.BigDecimal;
import java.util.UUID;

@FeignClient(name = "wallet-service")
public interface WalletServiceClient {
    @PostMapping("/api/v1/wallets/{walletId}/debit")
    Object debit(@PathVariable UUID walletId, @RequestHeader("Idempotency-Key") String idempotencyKey, @RequestBody WalletMoneyRequest request);

    @PostMapping("/api/v1/wallets/{walletId}/credit")
    Object credit(@PathVariable UUID walletId, @RequestHeader("Idempotency-Key") String idempotencyKey, @RequestBody WalletMoneyRequest request);

    record WalletMoneyRequest(
            BigDecimal amount,
            String currency,
            String category,
            UUID referenceId,
            String description
    ) {}
}
