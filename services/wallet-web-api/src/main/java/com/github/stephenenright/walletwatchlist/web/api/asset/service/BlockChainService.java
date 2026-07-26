/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.asset.service;

import com.github.stephenenright.walletwatchlist.web.api.asset.domain.BlockChain;
import com.github.stephenenright.walletwatchlist.web.api.asset.repository.BlockChainRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BlockChainService {

	private final BlockChainRepository blockChainRepository;

	@Transactional(readOnly = true)
	public Page<BlockChain> findAll(Pageable pageable) {
		return blockChainRepository.findAll(pageable);
	}

	@Transactional(readOnly = true)
	public Optional<BlockChain> findById(UUID id) {
		return blockChainRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Optional<BlockChain> findByCode(String code) {
		return blockChainRepository.findByCode(code);
	}

	@Transactional
	public BlockChain save(BlockChain blockChain) {
		return blockChainRepository.save(blockChain);
	}

	@Transactional
	public void deleteById(UUID id) {
		blockChainRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public boolean existsByCode(String code) {
		return blockChainRepository.existsByCode(code);
	}
}
