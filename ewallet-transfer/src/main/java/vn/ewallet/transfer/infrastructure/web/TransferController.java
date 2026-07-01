package vn.ewallet.transfer.infrastructure.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.ewallet.common.idempotency.IdempotencyKey;
import vn.ewallet.common.response.ApiResponse;
import vn.ewallet.transfer.application.service.TransferApplicationService;
import vn.ewallet.transfer.infrastructure.web.dto.CreateTransferRequest;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transfers")
@RequiredArgsConstructor
public class TransferController {
    private final TransferApplicationService transferService;

    @PostMapping
    public ResponseEntity<?> createTransfer(@RequestHeader("Idempotency-Key") String idempotency, @Valid @RequestBody CreateTransferRequest request) {
        new IdempotencyKey(idempotency);
        return ResponseEntity.ok(ApiResponse.success(
                transferService.createTransfer(request, idempotency)
        ));
    }

    @GetMapping("/{transferId}")
    public ResponseEntity<?> getTransfer(@PathVariable UUID transferId) {
        return ResponseEntity.ok(
                ApiResponse.success(transferService.getTransfer(transferId))
        );
    }

    @GetMapping()
    public ResponseEntity<?> getTransferByWallet(
            @RequestParam UUID walletId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        transferService.getTransferByWallet(walletId, PageRequest.of(page, size))
                )
        );
    }

    @DeleteMapping("/{transferId}/cancel")
    public ResponseEntity<?> cancelTransfer(@PathVariable UUID transferId) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        transferService.cancelTransfer(transferId)
                )
        );
    }
}
