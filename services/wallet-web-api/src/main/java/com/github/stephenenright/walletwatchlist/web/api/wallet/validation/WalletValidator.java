/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.wallet.validation;

import com.github.stephenenright.walletwatchlist.web.api.asset.repository.BlockChainRepository;
import com.github.stephenenright.walletwatchlist.web.api.common.validation.BaseValidator;
import com.github.stephenenright.walletwatchlist.web.api.common.validation.SpringValidator;
import com.github.stephenenright.walletwatchlist.web.api.common.validation.ValidationErrorBuilder;
import com.github.stephenenright.walletwatchlist.web.api.common.validation.ValidationResult;
import com.github.stephenenright.walletwatchlist.web.api.wallet.dto.request.CreateWalletRequest;
import com.github.stephenenright.walletwatchlist.web.api.wallet.dto.request.UpdateWalletRequest;
import com.github.stephenenright.walletwatchlist.web.api.wallet.repository.WalletRepository;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class WalletValidator extends BaseValidator {

	private final WalletRepository walletRepository;
	private final BlockChainRepository blockChainRepository;

	public WalletValidator(SpringValidator validator, WalletRepository walletRepository,
			BlockChainRepository blockChainRepository) {
		super(validator);
		this.walletRepository = walletRepository;
		this.blockChainRepository = blockChainRepository;
	}

	public ValidationResult validateForCreate(CreateWalletRequest request) {
		ValidationErrorBuilder builder = validate(request);

		if (builder.isValid()) {
			if (request.getBlockChainId() != null && !blockChainRepository.existsById(request.getBlockChainId())) {
				builder.addNotFoundError("blockChainId");
			} else if (request.getAddress() != null && request.getBlockChainId() != null && walletRepository
					.existsByAddressAndBlockChainId(request.getAddress(), request.getBlockChainId())) {
				builder.addExistsError("address");
			}
		}

		if (builder.hasErrors()) {
			return ValidationResult.invalid(builder.getErrors());
		}

		return ValidationResult.valid();
	}

	public ValidationResult validateForUpdate(UUID walletId, UpdateWalletRequest request) {
		ValidationErrorBuilder builder = validate(request);

		if (builder.isValid()) {
			if (!walletRepository.existsById(walletId)) {
				builder.addNotFoundError("id");
			}
		}

		if (builder.hasErrors()) {
			return ValidationResult.invalid(builder.getErrors());
		}

		return ValidationResult.valid();
	}
}
