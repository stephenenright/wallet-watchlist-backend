/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.wallet.repository;

import com.github.stephenenright.walletwatchlist.web.api.common.repository.BaseJpaRepository;
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.Wallet;
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WalletStatus;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends BaseJpaRepository<Wallet, UUID>, JpaSpecificationExecutor<Wallet> {

	Page<Wallet> findAllByOrderByDateCreatedDesc(Pageable pageable);

	Page<Wallet> findAllByStatusOrderByDateCreatedDesc(WalletStatus status, Pageable pageable);

	@Query("SELECT w FROM Wallet w WHERE LOWER(w.address) = LOWER(:address) AND w.blockChain.id = :blockChainId")
	Optional<Wallet> findByAddressAndBlockChainId(String address, UUID blockChainId);

	@Query("SELECT CASE WHEN COUNT(w) > 0 THEN true ELSE false END FROM Wallet w WHERE LOWER(w.address) = LOWER(:address) AND w.blockChain.id = :blockChainId")
	boolean existsByAddressAndBlockChainId(String address, UUID blockChainId);
}
