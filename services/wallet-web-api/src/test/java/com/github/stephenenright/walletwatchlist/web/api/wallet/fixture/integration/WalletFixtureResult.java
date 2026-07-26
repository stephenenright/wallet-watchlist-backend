package com.github.stephenenright.walletwatchlist.web.api.wallet.fixture.integration;

import com.github.stephenenright.walletwatchlist.web.api.asset.fixture.integration.AssetFixtureResult;
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.Wallet;
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WalletActivity;
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WalletAsset;
import java.util.List;
import lombok.Builder;

@Builder(toBuilder = true)
public record WalletFixtureResult(AssetFixtureResult assets, Wallet ethWallet1, Wallet ethWallet2, Wallet btcWallet,
		List<WalletAsset> ethWallet1Assets, List<WalletActivity> ethWallet1Activity) {
}
