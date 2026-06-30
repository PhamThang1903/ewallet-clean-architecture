package vn.ewallet.wallet.infrastructure.adapter.in.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.ewallet.common.response.ApiResponse;
import vn.ewallet.wallet.application.dto.CreateWalletCommand;
import vn.ewallet.wallet.application.dto.MoneyCommand;
import vn.ewallet.wallet.application.service.WalletApplicationService;
import vn.ewallet.wallet.infrastructure.adapter.in.web.dto.CreateWalletRequest;
import vn.ewallet.wallet.infrastructure.adapter.in.web.dto.MoneyRequest;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
public class WalletController {
    private final WalletApplicationService walletService;

    @PostMapping
    public ResponseEntity<?> createWallet(@RequestHeader("X-User-Id") UUID userId, @RequestBody CreateWalletRequest request) {
        var response = walletService.createWallet(
                new CreateWalletCommand(
                        userId,
                        request.currency() == null? "VND" : request.currency(),
                        request.walletType() == null ? "MAIN" : request.walletType()
                )
        );

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyWallet(@RequestHeader("X-User-Id") UUID userId) {
        return ResponseEntity.ok(
                ApiResponse.success(walletService.getMainWalletByUserId(userId))
        );
    }

    @GetMapping("/{walletId}/balance")
    public ResponseEntity<?> geBalance(@PathVariable UUID walletId) {
        return ResponseEntity.ok(
                ApiResponse.success(walletService.getBalance(walletId))
        );
    }

    @GetMapping("/{walletId}/transactions")
    public ResponseEntity<?> getTransactions(@PathVariable UUID walletId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(
                ApiResponse.success("Wallet frozen successfully", null)
        );
    }

    @PostMapping("/{walletId}/credit")
    public ResponseEntity<?> credit(@PathVariable UUID walletId, @RequestHeader("Idempotency-Key") String idempotencyKey, @RequestBody MoneyRequest request) {
        var response = walletService.credit(
               new MoneyCommand(
                       walletId,
                       request.amount(),
                       request.currency() == null ? "VND": request.currency(),
                       request.category() == null ? "ADJUSTMENT" : request.category(),
                       request.referenceId(),
                       idempotencyKey,
                       request.description()
               )
        );

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/{walletId}/debit")
    public ResponseEntity<?> debit(@PathVariable UUID walletId, @RequestHeader("Idempotency-Key") String idempotencyKey, @RequestBody MoneyRequest request) {
        var response = walletService.debit(
                new MoneyCommand(
                        walletId,
                        request.amount(),
                        request.currency() == null ? "VND": request.currency(),
                        request.category() == null ? "ADJUSTMENT" : request.category(),
                        request.referenceId(),
                        idempotencyKey,
                        request.description()
                )
        );

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
