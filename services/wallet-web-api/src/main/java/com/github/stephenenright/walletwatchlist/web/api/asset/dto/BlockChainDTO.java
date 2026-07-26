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
@Schema(description = "Blockchain network")
public class BlockChainDTO {

	@Schema(description = "Unique identifier", example = "b0000000-0000-0000-0000-000000000001")
	private UUID id;

	@Schema(description = "Blockchain code", example = "ETHEREUM")
	private String code;

	@Schema(description = "Blockchain name", example = "Ethereum")
	private String name;

	@Schema(description = "Network type", example = "mainnet")
	private String mainnet;

	@Schema(description = "Native currency symbol", example = "ETH")
	private String nativeCurrency;

	@Schema(description = "Date the blockchain was created")
	private Instant dateCreated;

	@Schema(description = "Date the blockchain was last updated")
	private Instant dateUpdated;
}
