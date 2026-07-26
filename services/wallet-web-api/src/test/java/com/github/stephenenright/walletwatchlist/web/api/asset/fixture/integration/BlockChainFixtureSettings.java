package com.github.stephenenright.walletwatchlist.web.api.asset.fixture.integration;

import lombok.Builder;

@Builder(toBuilder = true)
public record BlockChainFixtureSettings(boolean createEthereum, boolean createBitcoin, boolean createArbitrum,
		boolean createOptimism, boolean createBase) {

	public static BlockChainFixtureSettingsBuilder builder() {
		return new BlockChainFixtureSettingsBuilder().createEthereum(true).createBitcoin(true).createArbitrum(true)
				.createOptimism(true).createBase(true);
	}
}
