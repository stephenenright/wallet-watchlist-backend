/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.wallet.controller;

import com.github.stephenenright.walletwatchlist.web.api.common.controller.BaseApiController;
import com.github.stephenenright.walletwatchlist.web.api.common.dto.PagedRequestDTO;
import com.github.stephenenright.walletwatchlist.web.api.common.response.ApiResponse;
import com.github.stephenenright.walletwatchlist.web.api.common.response.CreateResponse;
import com.github.stephenenright.walletwatchlist.web.api.common.response.DeleteResponse;
import com.github.stephenenright.walletwatchlist.web.api.common.response.GetResponse;
import com.github.stephenenright.walletwatchlist.web.api.common.response.UpdateResponse;
import com.github.stephenenright.walletwatchlist.web.api.wallet.dto.WalletDTO;
import com.github.stephenenright.walletwatchlist.web.api.wallet.dto.WalletDetailDTO;
import com.github.stephenenright.walletwatchlist.web.api.wallet.dto.request.CreateWalletRequest;
import com.github.stephenenright.walletwatchlist.web.api.wallet.dto.request.SearchWalletRequest;
import com.github.stephenenright.walletwatchlist.web.api.wallet.dto.request.UpdateWalletRequest;
import com.github.stephenenright.walletwatchlist.web.api.wallet.mapper.WalletMapper;
import com.github.stephenenright.walletwatchlist.web.api.wallet.service.WalletGetService;
import com.github.stephenenright.walletwatchlist.web.api.wallet.service.WalletUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
@Tag(name = "Wallets", description = "Wallet management APIs")
public class WalletAPIController extends BaseApiController<WalletDTO> {

	private final WalletGetService walletGetService;
	private final WalletUpdateService walletUpdateService;
	private final WalletMapper walletMapper;

	@GetMapping
	@Operation(summary = "List all wallets", description = "Returns a paginated list of wallets with optional filtering by status")
	@ApiResponses({
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved wallets")})
	public ResponseEntity<ApiResponse<WalletDTO>> list(@ParameterObject SearchWalletRequest searchRequest,
			@ParameterObject PagedRequestDTO pagedRequest) {
		return apiListPaged(() -> {
			var page = walletGetService.findAll(searchRequest, pagedRequest.toPageable()).map(walletMapper::toDto);
			return GetResponse.createSuccessResponse(page);
		});
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get wallet by ID", description = "Returns detailed wallet information including balances and recent activity")
	@ApiResponses({
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved wallet details"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Wallet not found")})
	public ResponseEntity<ApiResponse<WalletDetailDTO>> getById(
			@Parameter(description = "Wallet ID", example = "00000000-0000-0000-0000-000000000001") @PathVariable UUID id) {
		return apiGet(() -> GetResponse.createSuccessOrNotFound(walletGetService.findDetailById(id).orElse(null)));
	}

	@PostMapping
	@Operation(summary = "Create a new wallet", description = "Creates a new wallet with the provided address and blockchain")
	@ApiResponses({
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Wallet created successfully"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request or validation failed")})
	public ResponseEntity<ApiResponse<WalletDetailDTO>> create(
			@Parameter(description = "Wallet creation request") @Valid @RequestBody CreateWalletRequest request) {
		return apiCreate(() -> {
			var createResponse = walletUpdateService.create(request);
			if (createResponse.isCreated() && createResponse.getResult().isPresent()) {
				return CreateResponse
						.createSuccessResponse(walletGetService.buildWalletDetail(createResponse.getResult().get()));
			}
			if (createResponse.hasValidationErrors() && createResponse.getValidationErrors().isPresent()) {
				return CreateResponse.createValidationFailedResponse(createResponse.getValidationErrors().get());
			}
			return CreateResponse.createFailedResponse(createResponse.getErrorMessage().orElse("Creation failed"));
		});
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update a wallet", description = "Updates an existing wallet's status and sync information")
	@ApiResponses({
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Wallet updated successfully"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Wallet not found"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request or validation failed")})
	public ResponseEntity<ApiResponse<WalletDetailDTO>> update(
			@Parameter(description = "Wallet ID", example = "00000000-0000-0000-0000-000000000001") @PathVariable UUID id,
			@Parameter(description = "Wallet update request") @Valid @RequestBody UpdateWalletRequest request) {
		return apiUpdate(() -> {
			var updateResponse = walletUpdateService.update(id, request);
			if (updateResponse.isUpdated() && updateResponse.getResult().isPresent()) {
				return UpdateResponse
						.createSuccessResponse(walletGetService.buildWalletDetail(updateResponse.getResult().get()));
			}
			if (updateResponse.isNotFound()) {
				return UpdateResponse.createNotFoundResponse();
			}
			if (updateResponse.getValidationErrors().isPresent()) {
				return UpdateResponse.createValidationFailedResponse(updateResponse.getValidationErrors().get());
			}
			return UpdateResponse.createFailedResponse(updateResponse.getErrorMessage().orElse("Update failed"));
		});
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete a wallet", description = "Deletes a wallet by its unique identifier")
	@ApiResponses({
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Wallet deleted successfully"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Wallet not found")})
	public ResponseEntity<ApiResponse<Void>> delete(
			@Parameter(description = "Wallet ID", example = "00000000-0000-0000-0000-000000000001") @PathVariable UUID id) {
		return apiDelete(() -> {
			var deleteResponse = walletUpdateService.delete(id);
			if (deleteResponse.isDeleted()) {
				return DeleteResponse.createSuccessResponse();
			}
			if (deleteResponse.isNotFound()) {
				return DeleteResponse.createNotFoundResponse();
			}
			return DeleteResponse.createFailureResponse(deleteResponse.getErrorMessage().orElse("Delete failed"));
		});
	}
}
