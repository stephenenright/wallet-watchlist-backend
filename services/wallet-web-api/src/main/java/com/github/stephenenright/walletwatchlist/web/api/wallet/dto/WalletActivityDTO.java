/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.wallet.dto;

import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.ActivityType;
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
@Schema(description = "Wallet activity information")
public class WalletActivityDTO {

	@Schema(description = "Unique identifier")
	private UUID id;

	@Schema(description = "Type of activity", example = "TRANSFER_IN")
	private ActivityType activityType;

	@Schema(description = "Human-readable summary of the activity")
	private String summary;

	@Schema(description = "Value of the activity in USD", example = "150.00")
	private BigDecimal valueUsd;

	@Schema(description = "Transaction hash (if associated with a transaction)")
	private String txHash;

	@Schema(description = "Date when the activity occurred")
	private Instant dateOccurred;
}
