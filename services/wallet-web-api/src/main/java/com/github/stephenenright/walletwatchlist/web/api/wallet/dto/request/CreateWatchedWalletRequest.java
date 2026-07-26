/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.wallet.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to create a watched wallet")
public class CreateWatchedWalletRequest {

	@NotNull(message = "Watcher ID is required")
	@Schema(description = "User ID of the watcher", example = "00000000-0000-0000-0000-000000000001", requiredMode = Schema.RequiredMode.REQUIRED)
	private UUID watcherId;

	@NotNull(message = "Wallet ID is required")
	@Schema(description = "Wallet ID to watch", example = "30000000-0000-0000-0000-000000000001", requiredMode = Schema.RequiredMode.REQUIRED)
	private UUID walletId;

	@Size(max = 100, message = "Label must be at most 100 characters")
	@Schema(description = "Optional label for the watched wallet", example = "My Savings Wallet")
	private String label;
}
