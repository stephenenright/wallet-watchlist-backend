/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.asset.service;

import com.github.stephenenright.walletwatchlist.web.api.asset.domain.BlockchainAsset;
import com.github.stephenenright.walletwatchlist.web.api.asset.repository.BlockchainAssetRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BlockchainAssetService {

	private final BlockchainAssetRepository blockchainAssetRepository;

	@Transactional(readOnly = true)
	public Page<BlockchainAsset> findAll(Pageable pageable) {
		return blockchainAssetRepository.findAllWithRelations(pageable);
	}

	@Transactional(readOnly = true)
	public Optional<BlockchainAsset> findById(UUID id) {
		return blockchainAssetRepository.findByIdWithRelations(id);
	}

	@Transactional(readOnly = true)
	public List<BlockchainAsset> findByCurrencyId(UUID currencyId) {
		return blockchainAssetRepository.findByCurrencyIdWithRelations(currencyId);
	}

	@Transactional(readOnly = true)
	public List<BlockchainAsset> findByBlockChainId(UUID blockChainId) {
		return blockchainAssetRepository.findByBlockChainIdWithRelations(blockChainId);
	}

	@Transactional(readOnly = true)
	public Optional<BlockchainAsset> findByCurrencyAndBlockChain(UUID currencyId, UUID blockChainId) {
		return blockchainAssetRepository.findByCurrencyIdAndBlockChainIdWithRelations(currencyId, blockChainId);
	}

	@Transactional(readOnly = true)
	public Optional<BlockchainAsset> findByContractAddressAndBlockChain(String contractAddress, UUID blockChainId) {
		return blockchainAssetRepository.findByContractAddressAndBlockChainIdWithRelations(contractAddress,
				blockChainId);
	}

	@Transactional
	public BlockchainAsset save(BlockchainAsset blockchainAsset) {
		return blockchainAssetRepository.save(blockchainAsset);
	}

	@Transactional
	public void deleteById(UUID id) {
		blockchainAssetRepository.deleteById(id);
	}
}
