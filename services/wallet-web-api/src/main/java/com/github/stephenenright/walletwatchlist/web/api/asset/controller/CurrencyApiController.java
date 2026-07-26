/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.asset.controller;

import com.github.stephenenright.walletwatchlist.web.api.asset.dto.CurrencyDTO;
import com.github.stephenenright.walletwatchlist.web.api.asset.mapper.CurrencyMapper;
import com.github.stephenenright.walletwatchlist.web.api.asset.service.CurrencyGetService;
import com.github.stephenenright.walletwatchlist.web.api.common.controller.BaseApiController;
import com.github.stephenenright.walletwatchlist.web.api.common.dto.PagedRequestDTO;
import com.github.stephenenright.walletwatchlist.web.api.common.response.ApiResponse;
import com.github.stephenenright.walletwatchlist.web.api.common.response.GetResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/currencies")
@RequiredArgsConstructor
@Tag(name = "Currencies", description = "Currency management APIs")
public class CurrencyApiController extends BaseApiController<CurrencyDTO> {

	private final CurrencyGetService currencyGetService;
	private final CurrencyMapper currencyMapper;

	@GetMapping
	@Operation(summary = "List all currencies", description = "Returns a paginated list of all currencies")
	@ApiResponses({
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved currencies")})
	public ResponseEntity<ApiResponse<CurrencyDTO>> list(@ParameterObject PagedRequestDTO pagedRequest) {
		return apiListPaged(() -> {
			var page = currencyGetService.getAll(pagedRequest.toPageable()).map(currencyMapper::toDto);
			return GetResponse.createSuccessResponse(page);
		});
	}

	@GetMapping("/{symbol}")
	@Operation(summary = "Get currency by symbol", description = "Returns a currency by its symbol")
	@ApiResponses({
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved currency"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Currency not found")})
	public ResponseEntity<ApiResponse<CurrencyDTO>> getBySymbol(
			@Parameter(description = "Currency symbol", example = "ETH") @PathVariable String symbol) {
		return apiGet(() -> GetResponse.createSuccessOrNotFound(
				currencyGetService.getBySymbol(symbol).map(currencyMapper::toDto).orElse(null)));
	}
}
