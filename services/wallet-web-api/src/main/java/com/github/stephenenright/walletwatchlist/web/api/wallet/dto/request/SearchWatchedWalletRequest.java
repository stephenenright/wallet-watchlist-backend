/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.wallet.dto.request;

import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WalletStatus;
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WatchedWalletStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Search criteria for watched wallets")
public class SearchWatchedWalletRequest {

	@Schema(description = "Filter by watcher (user) ID")
	private UUID watcherId;

	@Schema(description = "Filter by wallet ID")
	private UUID walletId;

	@Schema(description = "Filter by watched wallet status")
	private WatchedWalletStatus status;

	@Schema(description = "Filter by the wallet status")
	private WalletStatus walletStatus;
}
