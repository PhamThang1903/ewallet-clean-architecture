package vn.ewallet.wallet.infrastructure.adapter.out.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
public class WalletTransactionId implements Serializable {
    private UUID id;
    private OffsetDateTime createdAt;

}
