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
@Schema(description = "A currency deployed on a specific blockchain")
public class BlockchainAssetDTO {

	@Schema(description = "Unique identifier", example = "a0000000-0000-0000-0001-000000000001")
	private UUID id;

	@Schema(description = "The currency")
	private CurrencyDTO currency;

	@Schema(description = "The blockchain where the asset is deployed")
	private BlockChainDTO blockChain;

	@Schema(description = "Contract address for tokens, null for native assets", example = "0xA0b86991c6218b36c1d19D4a2e9Eb0cE3606eB48")
	private String contractAddress;

	@Schema(description = "True if this is the native asset of the blockchain", example = "false")
	private boolean nativeAsset;

	@Schema(description = "Date the asset was created")
	private Instant dateCreated;

	@Schema(description = "Date the asset was last updated")
	private Instant dateUpdated;
}
