/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.wallet.dto.request;

import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WatchedWalletStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to update a watched wallet")
public class UpdateWatchedWalletRequest {

	@Size(max = 100, message = "Label must be at most 100 characters")
	@Schema(description = "Label for the watched wallet", example = "My Savings Wallet")
	private String label;

	@Schema(description = "Watched wallet status", example = "ACTIVE")
	private WatchedWalletStatus status;
}
