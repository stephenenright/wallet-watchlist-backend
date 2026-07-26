/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.wallet.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
@Schema(description = "Request to create a new wallet")
public class CreateWalletRequest {

	@NotBlank(message = "Address is required")
	@Size(max = 255, message = "Address must be at most 255 characters")
	@Schema(description = "Wallet address", example = "0x742d35Cc6634C0532925a3b844Bc9e7595f8fE14", requiredMode = Schema.RequiredMode.REQUIRED)
	private String address;

	@NotNull(message = "Blockchain ID is required")
	@Schema(description = "Blockchain ID", example = "20000000-0000-0000-0000-000000000001", requiredMode = Schema.RequiredMode.REQUIRED)
	private UUID blockChainId;
}
