package com.github.stephenenright.walletwatchlist.web.api.wallet.fixture;

import com.github.stephenenright.walletwatchlist.web.api.asset.domain.BlockchainAsset;
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.Wallet;
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WalletAsset;
import java.math.BigDecimal;
import java.time.Instant;

public final class WalletAssetFixtureHelper {

	private WalletAssetFixtureHelper() {
	}

	public static WalletAsset createWalletAsset(Wallet wallet, BlockchainAsset asset, BigDecimal quantity) {
		WalletAsset walletAsset = WalletAsset.builder().wallet(wallet).blockchainAsset(asset).quantity(quantity)
				.dateCreated(Instant.now()).dateUpdated(Instant.now()).build();
		wallet.getAssets().add(walletAsset);
		return walletAsset;
	}

	public static WalletAsset createWalletAsset(Wallet wallet, BlockchainAsset asset, String quantity) {
		return createWalletAsset(wallet, asset, new BigDecimal(quantity));
	}
}
