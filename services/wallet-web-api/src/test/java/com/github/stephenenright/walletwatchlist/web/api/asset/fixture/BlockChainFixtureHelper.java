package com.github.stephenenright.walletwatchlist.web.api.asset.fixture;

import com.github.stephenenright.walletwatchlist.web.api.asset.domain.BlockChain;
import java.time.Instant;

public final class BlockChainFixtureHelper {

	private BlockChainFixtureHelper() {
	}

	public static BlockChain createEthereum() {
		return createBlockChain("ETHEREUM", "Ethereum", "mainnet", "ETH");
	}

	public static BlockChain createBitcoin() {
		return createBlockChain("BITCOIN", "Bitcoin", "mainnet", "BTC");
	}

	public static BlockChain createArbitrum() {
		return createBlockChain("ARBITRUM", "Arbitrum One", "mainnet", "ETH");
	}

	public static BlockChain createOptimism() {
		return createBlockChain("OPTIMISM", "Optimism", "mainnet", "ETH");
	}

	public static BlockChain createBase() {
		return createBlockChain("BASE", "Base", "mainnet", "ETH");
	}

	public static BlockChain createBlockChain(String code, String name, String mainnet, String nativeCurrency) {
		return BlockChain.builder().code(code).name(name).mainnet(mainnet).nativeCurrency(nativeCurrency)
				.dateCreated(Instant.now()).dateUpdated(Instant.now()).build();
	}
}
