package com.github.stephenenright.walletwatchlist.web.api.asset.fixture.integration;

import lombok.Builder;

@Builder(toBuilder = true)
public record BlockchainAssetFixtureSettings(boolean createBtcOnBitcoin, boolean createEthOnEthereum,
		boolean createUsdcOnEthereum, boolean createUsdtOnEthereum, boolean createDaiOnEthereum,
		boolean createWbtcOnEthereum) {

	public static BlockchainAssetFixtureSettingsBuilder builder() {
		return new BlockchainAssetFixtureSettingsBuilder().createBtcOnBitcoin(true).createEthOnEthereum(true)
				.createUsdcOnEthereum(true).createUsdtOnEthereum(true).createDaiOnEthereum(true)
				.createWbtcOnEthereum(true);
	}
}
