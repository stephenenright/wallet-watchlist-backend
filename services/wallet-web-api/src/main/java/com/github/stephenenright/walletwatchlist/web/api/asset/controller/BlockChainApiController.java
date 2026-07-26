/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.asset.controller;

import com.github.stephenenright.walletwatchlist.web.api.asset.dto.BlockChainDTO;
import com.github.stephenenright.walletwatchlist.web.api.asset.mapper.BlockChainMapper;
import com.github.stephenenright.walletwatchlist.web.api.asset.service.BlockChainService;
import com.github.stephenenright.walletwatchlist.web.api.common.controller.BaseApiController;
import com.github.stephenenright.walletwatchlist.web.api.common.dto.PagedRequestDTO;
import com.github.stephenenright.walletwatchlist.web.api.common.response.ApiResponse;
import com.github.stephenenright.walletwatchlist.web.api.common.response.GetResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/blockchains")
@RequiredArgsConstructor
@Tag(name = "Blockchains", description = "Blockchain network management APIs")
public class BlockChainApiController extends BaseApiController<BlockChainDTO> {

	private final BlockChainService blockChainService;
	private final BlockChainMapper blockChainMapper;

	@GetMapping
	@Operation(summary = "List all blockchains", description = "Returns a paginated list of all supported blockchain networks")
	@ApiResponses({
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved blockchains")})
	public ResponseEntity<ApiResponse<BlockChainDTO>> list(@ParameterObject PagedRequestDTO pagedRequest) {
		return apiListPaged(() -> {
			var page = blockChainService.findAll(pagedRequest.toPageable()).map(blockChainMapper::toDto);
			return GetResponse.createSuccessResponse(page);
		});
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get blockchain by ID", description = "Returns a blockchain by its unique identifier")
	@ApiResponses({
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved blockchain"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Blockchain not found")})
	public ResponseEntity<ApiResponse<BlockChainDTO>> getById(
			@Parameter(description = "Blockchain ID") @PathVariable UUID id) {
		return apiGet(() -> GetResponse
				.createSuccessOrNotFound(blockChainService.findById(id).map(blockChainMapper::toDto).orElse(null)));
	}

	@GetMapping("/code/{code}")
	@Operation(summary = "Get blockchain by code", description = "Returns a blockchain by its code")
	@ApiResponses({
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved blockchain"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Blockchain not found")})
	public ResponseEntity<ApiResponse<BlockChainDTO>> getByCode(
			@Parameter(description = "Blockchain code", example = "ETHEREUM") @PathVariable String code) {
		return apiGet(() -> GetResponse
				.createSuccessOrNotFound(blockChainService.findByCode(code).map(blockChainMapper::toDto).orElse(null)));
	}
}
