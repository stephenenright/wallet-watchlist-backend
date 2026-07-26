package com.github.stephenenright.walletwatchlist.web.api.wallet.fixture.integration;

import com.github.stephenenright.walletwatchlist.web.api.asset.domain.BlockChain;
import com.github.stephenenright.walletwatchlist.web.api.asset.domain.BlockchainAsset;
import com.github.stephenenright.walletwatchlist.web.api.asset.fixture.integration.AssetFixtureResult;
import com.github.stephenenright.walletwatchlist.web.api.asset.fixture.integration.BlockChainFixtureResult;
import com.github.stephenenright.walletwatchlist.web.api.asset.fixture.integration.BlockchainAssetFixtureResult;
import com.github.stephenenright.walletwatchlist.web.api.asset.repository.BlockChainRepository;
import com.github.stephenenright.walletwatchlist.web.api.asset.repository.BlockchainAssetRepository;
import com.github.stephenenright.walletwatchlist.web.api.common.jpa.JpaHelper;
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.Wallet;
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WalletActivity;
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WalletAsset;
import com.github.stephenenright.walletwatchlist.web.api.wallet.fixture.WalletActivityFixtureHelper;
import com.github.stephenenright.walletwatchlist.web.api.wallet.fixture.WalletAssetFixtureHelper;
import com.github.stephenenright.walletwatchlist.web.api.wallet.fixture.WalletFixtureHelper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WalletIntegrationTestHelper {

	private static final String ETH_WALLET_1_ADDRESS = "0x1111111111111111111111111111111111111111";
	private static final String ETH_WALLET_2_ADDRESS = "0x2222222222222222222222222222222222222222";
	private static final String BTC_WALLET_ADDRESS = "bc1qtest3333333333333333333333333333333333";
	private static final String USDC_ETH_CONTRACT = "0xA0b86991c6218b36c1d19D4a2e9Eb0cE3606eB48";

	private final JpaHelper jpaHelper;
	private final BlockChainRepository blockChainRepository;
	private final BlockchainAssetRepository blockchainAssetRepository;

	public WalletFixtureResult create(WalletFixtureSettings settings) {
		AssetFixtureResult assets = loadExistingAssets();

		var fixture = WalletFixtureResult.builder().assets(assets).build();

		fixture = createEthWallet1(fixture, settings, assets);
		fixture = createEthWallet2(fixture, settings, assets);
		fixture = createBtcWallet(fixture, settings, assets);

		if (settings.createAssets() && fixture.ethWallet1() != null) {
			fixture = createEthWallet1Assets(fixture, assets);
		}

		if (settings.createActivity() && fixture.ethWallet1() != null) {
			fixture = createEthWallet1Activity(fixture);
		}

		return fixture;
	}

	private AssetFixtureResult loadExistingAssets() {
		BlockChain ethereum = blockChainRepository.findByCode("ETHEREUM").orElse(null);
		BlockChain bitcoin = blockChainRepository.findByCode("BITCOIN").orElse(null);

		BlockChainFixtureResult blockChains = BlockChainFixtureResult.builder().ethereum(ethereum).bitcoin(bitcoin)
				.build();

		BlockchainAsset ethOnEthereum = null;
		BlockchainAsset usdcOnEthereum = null;
		BlockchainAsset btcOnBitcoin = null;

		if (ethereum != null) {
			ethOnEthereum = blockchainAssetRepository.findByBlockChainIdAndIsNativeTrue(ethereum.getId()).orElse(null);
			usdcOnEthereum = blockchainAssetRepository
					.findByContractAddressIgnoreCaseAndBlockChainId(USDC_ETH_CONTRACT, ethereum.getId()).orElse(null);
		}

		if (bitcoin != null) {
			btcOnBitcoin = blockchainAssetRepository.findByBlockChainIdAndIsNativeTrue(bitcoin.getId()).orElse(null);
		}

		BlockchainAssetFixtureResult blockchainAssets = BlockchainAssetFixtureResult.builder()
				.ethOnEthereum(ethOnEthereum).usdcOnEthereum(usdcOnEthereum).btcOnBitcoin(btcOnBitcoin).build();

		return AssetFixtureResult.builder().blockChains(blockChains).blockchainAssets(blockchainAssets).build();
	}

	private WalletFixtureResult createEthWallet1(WalletFixtureResult fixture, WalletFixtureSettings settings,
			AssetFixtureResult assets) {
		if (!settings.createEthWallet1() || assets.blockChains().ethereum() == null) {
			return fixture;
		}
		Wallet wallet = jpaHelper.save(
				WalletFixtureHelper.createWallet(ETH_WALLET_1_ADDRESS, assets.blockChains().ethereum()), Wallet.class);
		return fixture.toBuilder().ethWallet1(wallet).build();
	}

	private WalletFixtureResult createEthWallet2(WalletFixtureResult fixture, WalletFixtureSettings settings,
			AssetFixtureResult assets) {
		if (!settings.createEthWallet2() || assets.blockChains().ethereum() == null) {
			return fixture;
		}
		Wallet wallet = jpaHelper.save(
				WalletFixtureHelper.createWallet(ETH_WALLET_2_ADDRESS, assets.blockChains().ethereum()), Wallet.class);
		return fixture.toBuilder().ethWallet2(wallet).build();
	}

	private WalletFixtureResult createBtcWallet(WalletFixtureResult fixture, WalletFixtureSettings settings,
			AssetFixtureResult assets) {
		if (!settings.createBtcWallet() || assets.blockChains().bitcoin() == null) {
			return fixture;
		}
		Wallet wallet = jpaHelper.save(
				WalletFixtureHelper.createWallet(BTC_WALLET_ADDRESS, assets.blockChains().bitcoin()), Wallet.class);
		return fixture.toBuilder().btcWallet(wallet).build();
	}

	private WalletFixtureResult createEthWallet1Assets(WalletFixtureResult fixture, AssetFixtureResult assets) {
		List<WalletAsset> walletAssets = new ArrayList<>();

		if (assets.blockchainAssets() != null && assets.blockchainAssets().ethOnEthereum() != null) {
			WalletAsset ethAsset = jpaHelper.save(WalletAssetFixtureHelper.createWalletAsset(fixture.ethWallet1(),
					assets.blockchainAssets().ethOnEthereum(), "2.5"), WalletAsset.class);
			walletAssets.add(ethAsset);
		}

		if (assets.blockchainAssets() != null && assets.blockchainAssets().usdcOnEthereum() != null) {
			WalletAsset usdcAsset = jpaHelper.save(WalletAssetFixtureHelper.createWalletAsset(fixture.ethWallet1(),
					assets.blockchainAssets().usdcOnEthereum(), "1500.00"), WalletAsset.class);
			walletAssets.add(usdcAsset);
		}

		return fixture.toBuilder().ethWallet1Assets(walletAssets).build();
	}

	private WalletFixtureResult createEthWallet1Activity(WalletFixtureResult fixture) {
		List<WalletActivity> activities = new ArrayList<>();
		Instant now = Instant.now();

		WalletActivity transferIn = jpaHelper.save(WalletActivityFixtureHelper.createTransferIn(fixture.ethWallet1(),
				"Received 1 ETH from 0xabc...", "2000.00", now.minus(1, ChronoUnit.HOURS)), WalletActivity.class);
		activities.add(transferIn);

		WalletActivity swap = jpaHelper.save(WalletActivityFixtureHelper.createSwap(fixture.ethWallet1(),
				"Swapped 0.5 ETH for 1000 USDC", "1000.00", now.minus(2, ChronoUnit.HOURS)), WalletActivity.class);
		activities.add(swap);

		WalletActivity transferOut = jpaHelper.save(WalletActivityFixtureHelper.createTransferOut(fixture.ethWallet1(),
				"Sent 500 USDC to 0xdef...", "500.00", now.minus(3, ChronoUnit.HOURS)), WalletActivity.class);
		activities.add(transferOut);

		return fixture.toBuilder().ethWallet1Activity(activities).build();
	}
}
