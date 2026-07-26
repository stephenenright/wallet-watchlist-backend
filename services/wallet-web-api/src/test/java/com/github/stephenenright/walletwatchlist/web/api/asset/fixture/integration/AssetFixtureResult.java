package com.github.stephenenright.walletwatchlist.web.api.asset.fixture.integration;

import lombok.Builder;

@Builder(toBuilder = true)
public record AssetFixtureResult(BlockChainFixtureResult blockChains, CurrencyFixtureResult currencies,
		BlockchainAssetFixtureResult blockchainAssets) {
}
