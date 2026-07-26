/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.wallet.service;

import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.Wallet;
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WalletActivity;
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WalletAsset;
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.specification.WalletSearchSpecification;
import com.github.stephenenright.walletwatchlist.web.api.wallet.dto.WalletDetailDTO;
import com.github.stephenenright.walletwatchlist.web.api.wallet.dto.request.SearchWalletRequest;
import com.github.stephenenright.walletwatchlist.web.api.wallet.mapper.WalletActivityMapper;
import com.github.stephenenright.walletwatchlist.web.api.wallet.mapper.WalletAssetMapper;
import com.github.stephenenright.walletwatchlist.web.api.wallet.mapper.WalletMapper;
import com.github.stephenenright.walletwatchlist.web.api.wallet.repository.WalletActivityRepository;
import com.github.stephenenright.walletwatchlist.web.api.wallet.repository.WalletAssetRepository;
import com.github.stephenenright.walletwatchlist.web.api.wallet.repository.WalletRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalletGetService {

	private static final int RECENT_ACTIVITY_LIMIT = 10;

	private final WalletRepository walletRepository;
	private final WalletAssetRepository walletAssetRepository;
	private final WalletActivityRepository walletActivityRepository;
	private final WalletMapper walletMapper;
	private final WalletAssetMapper walletAssetMapper;
	private final WalletActivityMapper walletActivityMapper;

	@Transactional(readOnly = true)
	public Page<Wallet> findAll(SearchWalletRequest searchRequest, Pageable pageable) {
		var specification = WalletSearchSpecification.fromRequest(searchRequest);
		return walletRepository.findAll(specification, pageable);
	}

	@Transactional(readOnly = true)
	public Optional<Wallet> findById(UUID id) {
		return walletRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Optional<WalletDetailDTO> findDetailById(UUID id) {
		return walletRepository.findById(id).map(this::buildWalletDetail);
	}

	@Transactional(readOnly = true)
	public Optional<Wallet> findByAddressAndBlockChainId(String address, UUID blockChainId) {
		return walletRepository.findByAddressAndBlockChainId(address, blockChainId);
	}

	@Transactional(readOnly = true)
	public WalletDetailDTO buildWalletDetail(Wallet wallet) {
		List<WalletAsset> assets = walletAssetRepository.findByWalletIdWithAssociations(wallet.getId());
		List<WalletActivity> activities = walletActivityRepository.findByWalletIdWithTransactionOrderByDateOccurredDesc(
				wallet.getId(), PageRequest.of(0, RECENT_ACTIVITY_LIMIT));

		WalletDetailDTO detail = walletMapper.toDetailDto(wallet);
		detail.setAssets(walletAssetMapper.toDtoList(assets));
		detail.setRecentActivity(walletActivityMapper.toDtoList(activities));

		return detail;
	}
}
