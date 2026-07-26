package com.github.stephenenright.walletwatchlist.web.api.wallet.fixture;

import com.github.stephenenright.walletwatchlist.web.api.user.domain.User;
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WatchedWallet;
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WatchedWalletStatus;
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.Wallet;
import java.time.Instant;

public final class WatchedWalletFixtureHelper {

	private WatchedWalletFixtureHelper() {
	}

	public static WatchedWallet createWatchedWallet(User watcher, Wallet wallet) {
		return createWatchedWallet(watcher, wallet, null);
	}

	public static WatchedWallet createWatchedWallet(User watcher, Wallet wallet, String label) {
		return WatchedWallet.builder().watcher(watcher).wallet(wallet).label(label).status(WatchedWalletStatus.ACTIVE)
				.dateCreated(Instant.now()).dateUpdated(Instant.now()).build();
	}

	public static WatchedWallet createWatchedWallet(User watcher, Wallet wallet, String label,
			WatchedWalletStatus status) {
		return WatchedWallet.builder().watcher(watcher).wallet(wallet).label(label).status(status)
				.dateCreated(Instant.now()).dateUpdated(Instant.now()).build();
	}
}
