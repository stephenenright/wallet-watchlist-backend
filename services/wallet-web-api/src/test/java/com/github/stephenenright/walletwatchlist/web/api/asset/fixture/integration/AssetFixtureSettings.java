package com.github.stephenenright.walletwatchlist.web.api.asset.fixture.integration;

import lombok.Builder;

@Builder(toBuilder = true)
public record AssetFixtureSettings(BlockChainFixtureSettings blockChainSettings,
		CurrencyFixtureSettings currencySettings, BlockchainAssetFixtureSettings blockchainAssetSettings) {

	public static AssetFixtureSettingsBuilder builder() {
		return new AssetFixtureSettingsBuilder().blockChainSettings(BlockChainFixtureSettings.builder().build())
				.currencySettings(null).blockchainAssetSettings(null);
	}
}
