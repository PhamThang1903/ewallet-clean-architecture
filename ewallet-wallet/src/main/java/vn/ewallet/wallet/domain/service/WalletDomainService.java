package vn.ewallet.wallet.domain.service;

import org.springframework.stereotype.Service;
import vn.ewallet.common.domain.Money;
import vn.ewallet.wallet.domain.model.TransactionCategory;
import vn.ewallet.wallet.domain.model.TransactionType;
import vn.ewallet.wallet.domain.model.Wallet;
import vn.ewallet.wallet.domain.model.WalletTransaction;

import java.util.UUID;

@Service
public class WalletDomainService {

    public WalletTransaction credit(
            Wallet wallet,
            Money amount,
            TransactionCategory category,
            UUID referenceId,
            String idempotencyKey,
            String description
    ) {
        Money before = wallet.getBalance();
        wallet.credit(amount);
        Money after = wallet.getBalance();
        return new WalletTransaction(
                idempotencyKey,
                wallet.getId(),
                TransactionType.CREDIT,
                category,
                amount,
                before,
                after,
                referenceId,
                description
        );
    }

    public WalletTransaction debit(
            Wallet wallet,
            Money amount,
            TransactionCategory category,
            UUID referenceId,
            String idempotencyKey,
            String description
    ) {
        Money before = wallet.getBalance();
        wallet.debit(amount);
        Money after = wallet.getBalance();
        return new WalletTransaction(
                idempotencyKey,
                wallet.getId(),
                TransactionType.DEBIT,
                category,
                amount,
                before,
                after,
                referenceId,
                description
        );
    }
}
