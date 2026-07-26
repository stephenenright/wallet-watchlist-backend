/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.wallet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
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
@Schema(description = "Wallet asset information")
public class WalletAssetDTO {

	@Schema(description = "Unique identifier")
	private UUID id;

	@Schema(description = "Currency symbol", example = "ETH")
	private String currencySymbol;

	@Schema(description = "Currency name", example = "Ethereum")
	private String currencyName;

	@Schema(description = "Whether this is the native currency of the blockchain")
	private boolean isNative;

	@Schema(description = "Quantity held in the asset's native units", example = "1.5")
	private BigDecimal quantity;

	@Schema(description = "Date the asset was last updated")
	private Instant dateUpdated;
}
