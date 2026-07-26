/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.wallet.repository;

import com.github.stephenenright.walletwatchlist.web.api.common.repository.BaseJpaRepository;
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WalletAsset;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletAssetRepository extends BaseJpaRepository<WalletAsset, UUID> {

	@Query("SELECT wa FROM WalletAsset wa " + "JOIN FETCH wa.blockchainAsset ba " + "JOIN FETCH ba.currency "
			+ "JOIN FETCH ba.blockChain " + "WHERE wa.wallet.id = :walletId " + "ORDER BY wa.quantity DESC")
	List<WalletAsset> findByWalletIdWithAssociations(UUID walletId);

	void deleteByWalletId(UUID walletId);
}
