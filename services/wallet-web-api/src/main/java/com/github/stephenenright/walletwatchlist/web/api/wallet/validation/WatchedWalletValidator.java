/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.wallet.validation;

import com.github.stephenenright.walletwatchlist.web.api.common.validation.BaseValidator;
import com.github.stephenenright.walletwatchlist.web.api.common.validation.SpringValidator;
import com.github.stephenenright.walletwatchlist.web.api.common.validation.ValidationErrorBuilder;
import com.github.stephenenright.walletwatchlist.web.api.common.validation.ValidationResult;
import com.github.stephenenright.walletwatchlist.web.api.user.repository.UserRepository;
import com.github.stephenenright.walletwatchlist.web.api.wallet.dto.request.CreateWatchedWalletRequest;
import com.github.stephenenright.walletwatchlist.web.api.wallet.dto.request.UpdateWatchedWalletRequest;
import com.github.stephenenright.walletwatchlist.web.api.wallet.repository.WalletRepository;
import com.github.stephenenright.walletwatchlist.web.api.wallet.repository.WatchedWalletRepository;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class WatchedWalletValidator extends BaseValidator {

	private final WatchedWalletRepository watchedWalletRepository;
	private final WalletRepository walletRepository;
	private final UserRepository userRepository;

	public WatchedWalletValidator(SpringValidator validator, WatchedWalletRepository watchedWalletRepository,
			WalletRepository walletRepository, UserRepository userRepository) {
		super(validator);
		this.watchedWalletRepository = watchedWalletRepository;
		this.walletRepository = walletRepository;
		this.userRepository = userRepository;
	}

	public ValidationResult validateForCreate(CreateWatchedWalletRequest request) {
		ValidationErrorBuilder builder = validate(request);

		if (builder.isValid()) {
			if (request.getWatcherId() != null && !userRepository.existsById(request.getWatcherId())) {
				builder.addNotFoundError("watcherId");
			}

			if (request.getWalletId() != null && !walletRepository.existsById(request.getWalletId())) {
				builder.addNotFoundError("walletId");
			}

			if (request.getWatcherId() != null && request.getWalletId() != null && watchedWalletRepository
					.existsByWatcherIdAndWalletId(request.getWatcherId(), request.getWalletId())) {
				builder.addExistsError("walletId");
			}
		}

		if (builder.hasErrors()) {
			return ValidationResult.invalid(builder.getErrors());
		}

		return ValidationResult.valid();
	}

	public ValidationResult validateForUpdate(UUID watchedWalletId, UpdateWatchedWalletRequest request) {
		ValidationErrorBuilder builder = validate(request);

		if (builder.isValid()) {
			if (!watchedWalletRepository.existsById(watchedWalletId)) {
				builder.addNotFoundError("id");
			}
		}

		if (builder.hasErrors()) {
			return ValidationResult.invalid(builder.getErrors());
		}

		return ValidationResult.valid();
	}
}
