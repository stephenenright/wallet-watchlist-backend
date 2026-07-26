/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.wallet.dto;

import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WatchedWalletStatus;
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
@Schema(description = "Watched wallet information")
public class WatchedWalletDTO {

	@Schema(description = "Unique identifier", example = "00000000-0000-0000-0000-000000000001")
	private UUID id;

	@Schema(description = "ID of the user watching the wallet")
	private UUID watcherId;

	@Schema(description = "Email of the user watching the wallet", example = "john.doe@example.com")
	private String watcherEmail;

	@Schema(description = "Wallet ID")
	private UUID walletId;

	@Schema(description = "Wallet address", example = "0x742d35Cc6634C0532925a3b844Bc454e4438f44e")
	private String walletAddress;

	@Schema(description = "Blockchain code for the wallet", example = "ETH")
	private String walletBlockChainCode;

	@Schema(description = "User-defined label for the wallet", example = "My Savings Wallet")
	private String label;

	@Schema(description = "Watched wallet status", example = "ACTIVE")
	private WatchedWalletStatus status;

	@Schema(description = "Date the watched wallet entry was created")
	private Instant dateCreated;

	@Schema(description = "Date the watched wallet entry was last updated")
	private Instant dateUpdated;
}
