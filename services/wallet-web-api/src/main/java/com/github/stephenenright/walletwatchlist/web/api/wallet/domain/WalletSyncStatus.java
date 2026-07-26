/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.wallet.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WalletSyncStatus {
	PENDING("Queued for initial sync"), SYNCING("Currently syncing"), SYNCED("Successfully synced"), FAILED(
			"Sync failed, will retry"), RETRYING("Retry in progress");

	private final String description;
}
