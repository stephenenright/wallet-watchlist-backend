/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.asset.controller;

import com.github.stephenenright.walletwatchlist.web.api.asset.dto.BlockchainAssetDTO;
import com.github.stephenenright.walletwatchlist.web.api.asset.mapper.BlockchainAssetMapper;
import com.github.stephenenright.walletwatchlist.web.api.asset.service.BlockchainAssetService;
import com.github.stephenenright.walletwatchlist.web.api.common.controller.BaseApiController;
import com.github.stephenenright.walletwatchlist.web.api.common.dto.PagedRequestDTO;
import com.github.stephenenright.walletwatchlist.web.api.common.response.ApiResponse;
import com.github.stephenenright.walletwatchlist.web.api.common.response.GetResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/blockchain-assets")
@RequiredArgsConstructor
@Tag(name = "Blockchain Assets", description = "Blockchain asset management APIs")
public class BlockchainAssetApiController extends BaseApiController<BlockchainAssetDTO> {

	private final BlockchainAssetService blockchainAssetService;
	private final BlockchainAssetMapper blockchainAssetMapper;

	@GetMapping
	@Operation(summary = "List all blockchain assets", description = "Returns a paginated list of all blockchain assets (currencies deployed on blockchains)")
	@ApiResponses({
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved blockchain assets")})
	public ResponseEntity<ApiResponse<BlockchainAssetDTO>> list(@ParameterObject PagedRequestDTO pagedRequest) {
		return apiListPaged(() -> {
			var page = blockchainAssetService.findAll(pagedRequest.toPageable()).map(blockchainAssetMapper::toDto);
			return GetResponse.createSuccessResponse(page);
		});
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get blockchain asset by ID", description = "Returns a blockchain asset by its unique identifier")
	@ApiResponses({
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved blockchain asset"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Blockchain asset not found")})
	public ResponseEntity<ApiResponse<BlockchainAssetDTO>> getById(
			@Parameter(description = "Blockchain asset ID") @PathVariable UUID id) {
		return apiGet(() -> GetResponse.createSuccessOrNotFound(
				blockchainAssetService.findById(id).map(blockchainAssetMapper::toDto).orElse(null)));
	}

	@GetMapping("/by-currency/{currencyId}")
	@Operation(summary = "Get assets by currency", description = "Returns all blockchain assets for a given currency")
	@ApiResponses({
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved blockchain assets")})
	public ResponseEntity<ApiResponse<List<BlockchainAssetDTO>>> getByCurrency(
			@Parameter(description = "Currency ID") @PathVariable UUID currencyId) {
		return apiList(() -> GetResponse.createSuccessResponse(
				blockchainAssetMapper.toDtoList(blockchainAssetService.findByCurrencyId(currencyId))));
	}

	@GetMapping("/by-blockchain/{blockchainId}")
	@Operation(summary = "Get assets by blockchain", description = "Returns all assets deployed on a given blockchain")
	@ApiResponses({
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved blockchain assets")})
	public ResponseEntity<ApiResponse<List<BlockchainAssetDTO>>> getByBlockchain(
			@Parameter(description = "Blockchain ID") @PathVariable UUID blockchainId) {
		return apiList(() -> GetResponse.createSuccessResponse(
				blockchainAssetMapper.toDtoList(blockchainAssetService.findByBlockChainId(blockchainId))));
	}

	@GetMapping("/lookup")
	@Operation(summary = "Lookup blockchain asset", description = "Find a blockchain asset by currency+blockchain or by contract address+blockchain")
	@ApiResponses({
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved blockchain asset"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Blockchain asset not found")})
	public ResponseEntity<ApiResponse<BlockchainAssetDTO>> lookup(
			@Parameter(description = "Currency ID") @RequestParam(required = false) UUID currencyId,
			@Parameter(description = "Blockchain ID") @RequestParam(required = false) UUID blockchainId,
			@Parameter(description = "Contract address", example = "0xA0b86991c6218b36c1d19D4a2e9Eb0cE3606eB48") @RequestParam(required = false) String contractAddress) {
		if (currencyId != null && blockchainId != null) {
			return apiGet(() -> GetResponse.createSuccessOrNotFound(
					blockchainAssetService.findByCurrencyAndBlockChain(currencyId, blockchainId)
							.map(blockchainAssetMapper::toDto).orElse(null)));
		}
		if (contractAddress != null && blockchainId != null) {
			return apiGet(() -> GetResponse.createSuccessOrNotFound(
					blockchainAssetService.findByContractAddressAndBlockChain(contractAddress, blockchainId)
							.map(blockchainAssetMapper::toDto).orElse(null)));
		}
		return apiGet(() -> GetResponse.createNotFoundResponse());
	}
}
