package com.github.stephenenright.walletwatchlist.web.api.wallet.fixture;

import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.ActivityType;
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.Wallet;
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WalletActivity;
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WalletTransaction;
import java.math.BigDecimal;
import java.time.Instant;

public final class WalletActivityFixtureHelper {

	private WalletActivityFixtureHelper() {
	}

	public static WalletActivity createWalletActivity(Wallet wallet, ActivityType activityType, String summary,
			BigDecimal valueUsd, Instant dateOccurred) {
		WalletActivity activity = WalletActivity.builder().wallet(wallet).activityType(activityType).summary(summary)
				.valueUsd(valueUsd).dateOccurred(dateOccurred).dateCreated(Instant.now()).dateUpdated(Instant.now())
				.build();
		wallet.getActivities().add(activity);
		return activity;
	}

	public static WalletActivity createWalletActivity(Wallet wallet, WalletTransaction transaction,
			ActivityType activityType, String summary, BigDecimal valueUsd, Instant dateOccurred) {
		WalletActivity activity = WalletActivity.builder().wallet(wallet).transaction(transaction)
				.activityType(activityType).summary(summary).valueUsd(valueUsd).dateOccurred(dateOccurred)
				.dateCreated(Instant.now()).dateUpdated(Instant.now()).build();
		wallet.getActivities().add(activity);
		return activity;
	}

	public static WalletActivity createTransferIn(Wallet wallet, String summary, String valueUsd,
			Instant dateOccurred) {
		return createWalletActivity(wallet, ActivityType.TRANSFER_IN, summary, new BigDecimal(valueUsd), dateOccurred);
	}

	public static WalletActivity createTransferOut(Wallet wallet, String summary, String valueUsd,
			Instant dateOccurred) {
		return createWalletActivity(wallet, ActivityType.TRANSFER_OUT, summary, new BigDecimal(valueUsd), dateOccurred);
	}

	public static WalletActivity createSwap(Wallet wallet, String summary, String valueUsd, Instant dateOccurred) {
		return createWalletActivity(wallet, ActivityType.SWAP, summary, new BigDecimal(valueUsd), dateOccurred);
	}
}
