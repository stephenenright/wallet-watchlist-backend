/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.wallet.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WalletStatus {
	ACTIVE("Recent on-chain activity, sync frequently"), DORMANT("No activity for extended period, sync less often");

	private final String description;
}
