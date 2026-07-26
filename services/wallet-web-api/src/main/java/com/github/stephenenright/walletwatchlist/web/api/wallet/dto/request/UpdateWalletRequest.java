/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.wallet.dto.request;

import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WalletStatus;
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WalletSyncStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to update a wallet")
public class UpdateWalletRequest {

	@Schema(description = "Wallet status", example = "ACTIVE")
	private WalletStatus status;

	@Schema(description = "Sync status", example = "SYNCED")
	private WalletSyncStatus syncStatus;

	@Min(value = 0, message = "Sync retry count must be at least 0")
	@Schema(description = "Number of sync retry attempts", example = "0")
	private Integer syncRetryCount;

	@Schema(description = "Date the wallet was last synced")
	private Instant dateLastSynced;

	@Schema(description = "Date of last activity on the wallet")
	private Instant dateLastActivity;
}
