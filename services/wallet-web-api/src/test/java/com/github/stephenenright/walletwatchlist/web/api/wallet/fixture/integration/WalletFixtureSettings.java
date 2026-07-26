package com.github.stephenenright.walletwatchlist.web.api.wallet.fixture.integration;

import lombok.Builder;

@Builder(toBuilder = true)
public record WalletFixtureSettings(boolean createEthWallet1, boolean createEthWallet2, boolean createBtcWallet,
		boolean createAssets, boolean createActivity) {

	public static WalletFixtureSettingsBuilder builder() {
		return new WalletFixtureSettingsBuilder().createEthWallet1(true).createEthWallet2(true).createBtcWallet(true)
				.createAssets(true).createActivity(true);
	}
}
