/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.wallet.dto;

import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WalletStatus;
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WalletSyncStatus;
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
@Schema(description = "Wallet information")
public class WalletDTO {

	@Schema(description = "Unique identifier", example = "00000000-0000-0000-0000-000000000001")
	private UUID id;

	@Schema(description = "Wallet address", example = "0x742d35Cc6634C0532925a3b844Bc454e4438f44e")
	private String address;

	@Schema(description = "Blockchain ID")
	private UUID blockChainId;

	@Schema(description = "Blockchain code", example = "ETH")
	private String blockChainCode;

	@Schema(description = "Blockchain name", example = "Ethereum Mainnet")
	private String blockChainName;

	@Schema(description = "Wallet status", example = "ACTIVE")
	private WalletStatus status;

	@Schema(description = "Sync status", example = "SYNCED")
	private WalletSyncStatus syncStatus;

	@Schema(description = "Number of sync retries", example = "0")
	private int syncRetryCount;

	@Schema(description = "Date the wallet was last synced")
	private Instant dateLastSynced;

	@Schema(description = "Date of last on-chain activity")
	private Instant dateLastActivity;

	@Schema(description = "Total wallet value in USD", example = "6500.00")
	private BigDecimal balanceUsd;

	@Schema(description = "Date the wallet was created")
	private Instant dateCreated;

	@Schema(description = "Date the wallet was last updated")
	private Instant dateUpdated;
}
