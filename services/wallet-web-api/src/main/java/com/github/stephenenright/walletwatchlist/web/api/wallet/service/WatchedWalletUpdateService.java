/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.wallet.service;

import com.github.stephenenright.walletwatchlist.web.api.common.response.CreateResponse;
import com.github.stephenenright.walletwatchlist.web.api.common.response.DeleteResponse;
import com.github.stephenenright.walletwatchlist.web.api.common.response.UpdateResponse;
import com.github.stephenenright.walletwatchlist.web.api.user.repository.UserRepository;
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WatchedWallet;
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WatchedWalletStatus;
import com.github.stephenenright.walletwatchlist.web.api.wallet.dto.request.CreateWatchedWalletRequest;
import com.github.stephenenright.walletwatchlist.web.api.wallet.dto.request.UpdateWatchedWalletRequest;
import com.github.stephenenright.walletwatchlist.web.api.wallet.repository.WalletRepository;
import com.github.stephenenright.walletwatchlist.web.api.wallet.repository.WatchedWalletRepository;
import com.github.stephenenright.walletwatchlist.web.api.wallet.validation.WatchedWalletValidator;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WatchedWalletUpdateService {

	private final WatchedWalletRepository watchedWalletRepository;
	private final WalletRepository walletRepository;
	private final UserRepository userRepository;
	private final WatchedWalletValidator watchedWalletValidator;

	@Transactional
	public CreateResponse<WatchedWallet> create(CreateWatchedWalletRequest request) {
		final var validationResult = watchedWalletValidator.validateForCreate(request);

		if (validationResult.isNotValid()) {
			return CreateResponse.createValidationFailedResponse(validationResult.getErrors());
		}

		var watcher = userRepository.findById(request.getWatcherId()).orElseThrow();
		var wallet = walletRepository.findById(request.getWalletId()).orElseThrow();

		WatchedWallet watchedWallet = WatchedWallet.builder().watcher(watcher).wallet(wallet).label(request.getLabel())
				.status(WatchedWalletStatus.ACTIVE).build();

		return CreateResponse.createSuccessResponse(watchedWalletRepository.save(watchedWallet));
	}

	@Transactional
	public UpdateResponse<WatchedWallet> update(UUID watchedWalletId, UpdateWatchedWalletRequest request) {
		final var validationResult = watchedWalletValidator.validateForUpdate(watchedWalletId, request);

		if (validationResult.isNotValid()) {
			if (validationResult.getErrors().containsKey("id")) {
				return UpdateResponse.createNotFoundResponse();
			}
			return UpdateResponse.createValidationFailedResponse(validationResult.getErrors());
		}

		var watchedWallet = watchedWalletRepository.findById(watchedWalletId).orElseThrow();

		if (request.getLabel() != null) {
			watchedWallet.setLabel(request.getLabel());
		}

		if (request.getStatus() != null) {
			watchedWallet.setStatus(request.getStatus());
		}

		return UpdateResponse.createSuccessResponse(watchedWalletRepository.save(watchedWallet));
	}

	@Transactional
	public DeleteResponse<Void> delete(UUID watchedWalletId) {
		var watchedWalletOpt = watchedWalletRepository.findById(watchedWalletId);

		if (watchedWalletOpt.isEmpty()) {
			return DeleteResponse.createNotFoundResponse();
		}

		watchedWalletRepository.delete(watchedWalletOpt.get());

		return DeleteResponse.createSuccessResponse();
	}
}
