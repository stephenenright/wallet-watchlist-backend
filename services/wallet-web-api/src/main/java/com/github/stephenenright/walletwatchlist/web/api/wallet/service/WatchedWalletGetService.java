/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.wallet.service;

import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WatchedWallet;
import com.github.stephenenright.walletwatchlist.web.api.wallet.dto.request.SearchWatchedWalletRequest;
import com.github.stephenenright.walletwatchlist.web.api.wallet.repository.WatchedWalletRepository;
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.specification.WatchedWalletSearchSpecification;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WatchedWalletGetService {

	private final WatchedWalletRepository watchedWalletRepository;

	@Transactional(readOnly = true)
	public Page<WatchedWallet> findAll(SearchWatchedWalletRequest searchRequest, Pageable pageable) {
		var specification = WatchedWalletSearchSpecification.fromRequest(searchRequest);
		return watchedWalletRepository.findAll(specification, pageable);
	}

	@Transactional(readOnly = true)
	public Optional<WatchedWallet> findById(UUID id) {
		return watchedWalletRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Optional<WatchedWallet> findByWatcherIdAndWalletId(UUID watcherId, UUID walletId) {
		return watchedWalletRepository.findByWatcherIdAndWalletId(watcherId, walletId);
	}
}
