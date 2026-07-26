/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.asset.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Currency representing a cryptocurrency or token")
public class CurrencyDTO {

	@Schema(description = "Unique identifier", example = "c0000000-0000-0000-0000-000000000002")
	private UUID id;

	@Schema(description = "Currency symbol", example = "ETH")
	private String symbol;

	@Schema(description = "Currency name", example = "Ethereum")
	private String name;

	@Schema(description = "Date the currency was created")
	private Instant dateCreated;

	@Schema(description = "Date the currency was last updated")
	private Instant dateUpdated;
}
