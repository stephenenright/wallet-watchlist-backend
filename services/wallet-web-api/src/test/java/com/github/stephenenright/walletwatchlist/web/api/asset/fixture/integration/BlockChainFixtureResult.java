package com.github.stephenenright.walletwatchlist.web.api.asset.fixture.integration;

import com.github.stephenenright.walletwatchlist.web.api.asset.domain.BlockChain;
import lombok.Builder;

@Builder(toBuilder = true)
public record BlockChainFixtureResult(BlockChain ethereum, BlockChain bitcoin, BlockChain arbitrum, BlockChain optimism,
		BlockChain base) {
}
