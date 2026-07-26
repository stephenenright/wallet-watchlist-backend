/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.wallet.repository;

import com.github.stephenenright.walletwatchlist.web.api.common.repository.BaseJpaRepository;
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WalletActivity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletActivityRepository extends BaseJpaRepository<WalletActivity, UUID> {

	@Query("SELECT wa FROM WalletActivity wa " + "LEFT JOIN FETCH wa.transaction " + "WHERE wa.wallet.id = :walletId "
			+ "ORDER BY wa.dateOccurred DESC")
	List<WalletActivity> findByWalletIdWithTransactionOrderByDateOccurredDesc(UUID walletId, Pageable pageable);

	List<WalletActivity> findByWalletIdOrderByDateOccurredDesc(UUID walletId, Pageable pageable);

	void deleteByWalletId(UUID walletId);
}
