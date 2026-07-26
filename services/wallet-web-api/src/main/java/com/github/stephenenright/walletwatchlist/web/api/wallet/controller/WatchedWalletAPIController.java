/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.wallet.controller;

import com.github.stephenenright.walletwatchlist.web.api.common.controller.BaseApiController;
import com.github.stephenenright.walletwatchlist.web.api.common.dto.PagedRequestDTO;
import com.github.stephenenright.walletwatchlist.web.api.common.response.ApiResponse;
import com.github.stephenenright.walletwatchlist.web.api.common.response.CreateResponse;
import com.github.stephenenright.walletwatchlist.web.api.common.response.DeleteResponse;
import com.github.stephenenright.walletwatchlist.web.api.common.response.GetResponse;
import com.github.stephenenright.walletwatchlist.web.api.common.response.UpdateResponse;
import com.github.stephenenright.walletwatchlist.web.api.wallet.dto.WatchedWalletDTO;
import com.github.stephenenright.walletwatchlist.web.api.wallet.dto.request.CreateWatchedWalletRequest;
import com.github.stephenenright.walletwatchlist.web.api.wallet.dto.request.SearchWatchedWalletRequest;
import com.github.stephenenright.walletwatchlist.web.api.wallet.dto.request.UpdateWatchedWalletRequest;
import com.github.stephenenright.walletwatchlist.web.api.wallet.mapper.WatchedWalletMapper;
import com.github.stephenenright.walletwatchlist.web.api.wallet.service.WatchedWalletGetService;
import com.github.stephenenright.walletwatchlist.web.api.wallet.service.WatchedWalletUpdateService;
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
@RequestMapping("/api/watched-wallets")
@RequiredArgsConstructor
@Tag(name = "Watched Wallets", description = "Watched wallet management APIs")
public class WatchedWalletAPIController extends BaseApiController<WatchedWalletDTO> {

	private final WatchedWalletGetService watchedWalletGetService;
	private final WatchedWalletUpdateService watchedWalletUpdateService;
	private final WatchedWalletMapper watchedWalletMapper;

	@GetMapping
	@Operation(summary = "List watched wallets", description = "Returns a paginated list of watched wallets with optional filtering by watcher, wallet, or status")
	@ApiResponses({
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved watched wallets")})
	public ResponseEntity<ApiResponse<WatchedWalletDTO>> list(@ParameterObject SearchWatchedWalletRequest searchRequest,
			@ParameterObject PagedRequestDTO pagedRequest) {
		return apiListPaged(() -> {
			var page = watchedWalletGetService.findAll(searchRequest, pagedRequest.toPageable())
					.map(watchedWalletMapper::toDto);
			return GetResponse.createSuccessResponse(page);
		});
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get watched wallet by ID", description = "Returns a watched wallet by its unique identifier")
	@ApiResponses({
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved watched wallet"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Watched wallet not found")})
	public ResponseEntity<ApiResponse<WatchedWalletDTO>> getById(
			@Parameter(description = "Watched wallet ID", example = "00000000-0000-0000-0000-000000000001") @PathVariable UUID id) {
		return apiGet(() -> GetResponse.createSuccessOrNotFound(
				watchedWalletGetService.findById(id).map(watchedWalletMapper::toDto).orElse(null)));
	}

	@PostMapping
	@Operation(summary = "Create a watched wallet", description = "Creates a new watched wallet linking a user to a wallet they want to watch")
	@ApiResponses({
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Watched wallet created successfully"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request or validation failed")})
	public ResponseEntity<ApiResponse<WatchedWalletDTO>> create(
			@Parameter(description = "Watched wallet creation request") @Valid @RequestBody CreateWatchedWalletRequest request) {
		return apiCreate(() -> {
			var createResponse = watchedWalletUpdateService.create(request);
			if (createResponse.isCreated() && createResponse.getResult().isPresent()) {
				return CreateResponse
						.createSuccessResponse(watchedWalletMapper.toDto(createResponse.getResult().get()));
			}
			if (createResponse.hasValidationErrors() && createResponse.getValidationErrors().isPresent()) {
				return CreateResponse.createValidationFailedResponse(createResponse.getValidationErrors().get());
			}
			return CreateResponse.createFailedResponse(createResponse.getErrorMessage().orElse("Creation failed"));
		});
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update a watched wallet", description = "Updates an existing watched wallet's label or status")
	@ApiResponses({
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Watched wallet updated successfully"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Watched wallet not found"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request or validation failed")})
	public ResponseEntity<ApiResponse<WatchedWalletDTO>> update(
			@Parameter(description = "Watched wallet ID", example = "00000000-0000-0000-0000-000000000001") @PathVariable UUID id,
			@Parameter(description = "Watched wallet update request") @Valid @RequestBody UpdateWatchedWalletRequest request) {
		return apiUpdate(() -> {
			var updateResponse = watchedWalletUpdateService.update(id, request);
			if (updateResponse.isUpdated() && updateResponse.getResult().isPresent()) {
				return UpdateResponse
						.createSuccessResponse(watchedWalletMapper.toDto(updateResponse.getResult().get()));
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
	@Operation(summary = "Delete a watched wallet", description = "Removes a watched wallet entry (does not delete the underlying wallet)")
	@ApiResponses({
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Watched wallet deleted successfully"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Watched wallet not found")})
	public ResponseEntity<ApiResponse<Void>> delete(
			@Parameter(description = "Watched wallet ID", example = "00000000-0000-0000-0000-000000000001") @PathVariable UUID id) {
		return apiDelete(() -> {
			var deleteResponse = watchedWalletUpdateService.delete(id);
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
