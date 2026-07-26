/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.asset.repository;

import com.github.stephenenright.walletwatchlist.web.api.asset.domain.BlockchainAsset;
import com.github.stephenenright.walletwatchlist.web.api.common.repository.BaseJpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockchainAssetRepository extends BaseJpaRepository<BlockchainAsset, UUID> {

	@Query(value = "SELECT ba FROM BlockchainAsset ba JOIN FETCH ba.currency JOIN FETCH ba.blockChain", countQuery = "SELECT COUNT(ba) FROM BlockchainAsset ba")
	Page<BlockchainAsset> findAllWithRelations(Pageable pageable);

	@Query("SELECT ba FROM BlockchainAsset ba JOIN FETCH ba.currency JOIN FETCH ba.blockChain WHERE ba.id = :id")
	Optional<BlockchainAsset> findByIdWithRelations(UUID id);

	@Query("SELECT ba FROM BlockchainAsset ba JOIN FETCH ba.currency JOIN FETCH ba.blockChain WHERE ba.currency.id = :currencyId")
	List<BlockchainAsset> findByCurrencyIdWithRelations(UUID currencyId);

	@Query("SELECT ba FROM BlockchainAsset ba JOIN FETCH ba.currency JOIN FETCH ba.blockChain WHERE ba.blockChain.id = :blockChainId")
	List<BlockchainAsset> findByBlockChainIdWithRelations(UUID blockChainId);

	@Query("SELECT ba FROM BlockchainAsset ba JOIN FETCH ba.currency JOIN FETCH ba.blockChain WHERE ba.currency.id = :currencyId AND ba.blockChain.id = :blockChainId")
	Optional<BlockchainAsset> findByCurrencyIdAndBlockChainIdWithRelations(UUID currencyId, UUID blockChainId);

	@Query("SELECT ba FROM BlockchainAsset ba JOIN FETCH ba.currency JOIN FETCH ba.blockChain WHERE ba.contractAddress = :contractAddress AND ba.blockChain.id = :blockChainId")
	Optional<BlockchainAsset> findByContractAddressAndBlockChainIdWithRelations(String contractAddress,
			UUID blockChainId);

	List<BlockchainAsset> findByCurrencyId(UUID currencyId);

	List<BlockchainAsset> findByBlockChainId(UUID blockChainId);

	Optional<BlockchainAsset> findByCurrencyIdAndBlockChainId(UUID currencyId, UUID blockChainId);

	Optional<BlockchainAsset> findByContractAddressAndBlockChainId(String contractAddress, UUID blockChainId);

	Optional<BlockchainAsset> findByContractAddressIgnoreCaseAndBlockChainId(String contractAddress, UUID blockChainId);

	Optional<BlockchainAsset> findByBlockChainIdAndIsNativeTrue(UUID blockChainId);
}
