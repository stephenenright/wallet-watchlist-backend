/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.wallet.dto.request;

import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WalletStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Search criteria for wallets")
public class SearchWalletRequest {

	@Schema(description = "Filter by wallet status", example = "ACTIVE")
	private WalletStatus status;
}
