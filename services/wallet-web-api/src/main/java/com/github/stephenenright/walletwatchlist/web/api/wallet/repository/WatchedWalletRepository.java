/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.wallet.repository;

import com.github.stephenenright.walletwatchlist.web.api.common.repository.BaseJpaRepository;
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WatchedWallet;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WatchedWalletRepository
		extends
			BaseJpaRepository<WatchedWallet, UUID>,
			JpaSpecificationExecutor<WatchedWallet> {

	Page<WatchedWallet> findAllByWatcherIdOrderByDateCreatedDesc(UUID watcherId, Pageable pageable);

	Page<WatchedWallet> findAllByWalletIdOrderByDateCreatedDesc(UUID walletId, Pageable pageable);

	@Query("SELECT ww FROM WatchedWallet ww WHERE ww.watcher.id = :watcherId AND ww.wallet.id = :walletId")
	Optional<WatchedWallet> findByWatcherIdAndWalletId(UUID watcherId, UUID walletId);

	@Query("SELECT CASE WHEN COUNT(ww) > 0 THEN true ELSE false END FROM WatchedWallet ww WHERE ww.watcher.id = :watcherId AND ww.wallet.id = :walletId")
	boolean existsByWatcherIdAndWalletId(UUID watcherId, UUID walletId);

	long countByWalletId(UUID walletId);
}
