/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.wallet.repository;

import com.github.stephenenright.walletwatchlist.web.api.common.repository.BaseJpaRepository;
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WalletTransaction;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletTransactionRepository extends BaseJpaRepository<WalletTransaction, UUID> {

	List<WalletTransaction> findByWalletIdOrderByDateOccurredDesc(UUID walletId, Pageable pageable);

	void deleteByWalletId(UUID walletId);
}
