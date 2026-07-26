/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.wallet.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WatchedWalletStatus {
	ACTIVE("Actively watching this wallet"), PAUSED("Watching paused, no notifications"), ARCHIVED(
			"No longer watching, kept for history");

	private final String description;
}
