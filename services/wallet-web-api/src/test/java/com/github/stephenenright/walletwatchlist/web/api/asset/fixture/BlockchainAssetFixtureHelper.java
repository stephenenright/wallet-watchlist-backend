package com.github.stephenenright.walletwatchlist.web.api.asset.fixture;

import com.github.stephenenright.walletwatchlist.web.api.asset.domain.BlockChain;
import com.github.stephenenright.walletwatchlist.web.api.asset.domain.BlockchainAsset;
import com.github.stephenenright.walletwatchlist.web.api.asset.domain.Currency;
import java.time.Instant;

public final class BlockchainAssetFixtureHelper {

	private BlockchainAssetFixtureHelper() {
	}

	public static BlockchainAsset createNativeAsset(Currency currency, BlockChain blockChain) {
		return BlockchainAsset.builder().currency(currency).blockChain(blockChain).isNative(true).contractAddress(null)
				.dateCreated(Instant.now()).dateUpdated(Instant.now()).build();
	}

	public static BlockchainAsset createTokenAsset(Currency currency, BlockChain blockChain, String contractAddress) {
		return BlockchainAsset.builder().currency(currency).blockChain(blockChain).isNative(false)
				.contractAddress(contractAddress).dateCreated(Instant.now()).dateUpdated(Instant.now()).build();
	}
}
