package com.github.stephenenright.walletwatchlist.web.api.asset.fixture.integration;

import com.github.stephenenright.walletwatchlist.web.api.asset.domain.BlockchainAsset;
import lombok.Builder;

@Builder(toBuilder = true)
public record BlockchainAssetFixtureResult(BlockchainAsset btcOnBitcoin, BlockchainAsset ethOnEthereum,
		BlockchainAsset usdcOnEthereum, BlockchainAsset usdtOnEthereum, BlockchainAsset daiOnEthereum,
		BlockchainAsset wbtcOnEthereum) {
}
