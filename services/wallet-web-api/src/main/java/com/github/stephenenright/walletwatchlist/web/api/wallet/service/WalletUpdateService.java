/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.wallet.service;

import com.github.stephenenright.walletwatchlist.web.api.asset.repository.BlockChainRepository;
import com.github.stephenenright.walletwatchlist.web.api.common.response.CreateResponse;
import com.github.stephenenright.walletwatchlist.web.api.common.response.DeleteResponse;
import com.github.stephenenright.walletwatchlist.web.api.common.response.ErrorType;
import com.github.stephenenright.walletwatchlist.web.api.common.response.UpdateResponse;
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.Wallet;
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WalletStatus;
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WalletSyncStatus;
import com.github.stephenenright.walletwatchlist.web.api.wallet.dto.request.CreateWalletRequest;
import com.github.stephenenright.walletwatchlist.web.api.wallet.dto.request.UpdateWalletRequest;
import com.github.stephenenright.walletwatchlist.web.api.wallet.repository.WalletActivityRepository;
import com.github.stephenenright.walletwatchlist.web.api.wallet.repository.WalletAssetRepository;
import com.github.stephenenright.walletwatchlist.web.api.wallet.repository.WalletRepository;
import com.github.stephenenright.walletwatchlist.web.api.wallet.repository.WalletTransactionRepository;
import com.github.stephenenright.walletwatchlist.web.api.wallet.repository.WatchedWalletRepository;
import com.github.stephenenright.walletwatchlist.web.api.wallet.validation.WalletValidator;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalletUpdateService {

	private final WalletRepository walletRepository;
	private final WalletAssetRepository walletAssetRepository;
	private final WalletActivityRepository walletActivityRepository;
	private final WalletTransactionRepository walletTransactionRepository;
	private final WatchedWalletRepository watchedWalletRepository;
	private final BlockChainRepository blockChainRepository;
	private final WalletValidator walletValidator;
	private final WalletCreationDataMockingService walletDataMockingService;

	@Transactional
	public CreateResponse<Wallet> create(CreateWalletRequest request) {
		final var validationResult = walletValidator.validateForCreate(request);

		if (validationResult.isNotValid()) {
			return CreateResponse.createValidationFailedResponse(validationResult.getErrors());
		}

		var blockChain = blockChainRepository.findById(request.getBlockChainId()).orElseThrow();

		Wallet wallet = Wallet.builder().address(request.getAddress()).blockChain(blockChain)
				.status(WalletStatus.ACTIVE).syncStatus(WalletSyncStatus.PENDING).syncRetryCount(0).build();

		return CreateResponse.createSuccessResponse(walletDataMockingService.mockWalletDataOnCreate(wallet));
	}

	@Transactional
	public UpdateResponse<Wallet> update(UUID walletId, UpdateWalletRequest request) {
		final var validationResult = walletValidator.validateForUpdate(walletId, request);

		if (validationResult.isNotValid()) {
			if (validationResult.getErrors().containsKey("id")) {
				return UpdateResponse.createNotFoundResponse();
			}
			return UpdateResponse.createValidationFailedResponse(validationResult.getErrors());
		}

		var wallet = walletRepository.findById(walletId).orElseThrow();

		if (request.getStatus() != null) {
			wallet.setStatus(request.getStatus());
		}

		if (request.getSyncStatus() != null) {
			wallet.setSyncStatus(request.getSyncStatus());
		}

		if (request.getSyncRetryCount() != null) {
			wallet.setSyncRetryCount(request.getSyncRetryCount());
		}

		if (request.getDateLastSynced() != null) {
			wallet.setDateLastSynced(request.getDateLastSynced());
		}

		if (request.getDateLastActivity() != null) {
			wallet.setDateLastActivity(request.getDateLastActivity());
		}

		return UpdateResponse.createSuccessResponse(walletRepository.save(wallet));
	}

	@Transactional
	public DeleteResponse<Void> delete(UUID walletId) {
		var walletOpt = walletRepository.findById(walletId);

		if (walletOpt.isEmpty()) {
			return DeleteResponse.createNotFoundResponse();
		}

		long watcherCount = watchedWalletRepository.countByWalletId(walletId);

		if (watcherCount > 0) {
			return DeleteResponse.createFailureResponse(
					"Cannot delete wallet with active watchers. Remove all watchers first.", ErrorType.BAD_REQUEST);
		}

		walletActivityRepository.deleteByWalletId(walletId);
		walletAssetRepository.deleteByWalletId(walletId);
		walletTransactionRepository.deleteByWalletId(walletId);
		walletRepository.delete(walletOpt.get());

		return DeleteResponse.createSuccessResponse();
	}
}
