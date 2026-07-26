package com.github.stephenenright.walletwatchlist.web.api.asset.fixture.integration;

import com.github.stephenenright.walletwatchlist.web.api.asset.domain.BlockChain;
import com.github.stephenenright.walletwatchlist.web.api.asset.domain.BlockchainAsset;
import com.github.stephenenright.walletwatchlist.web.api.asset.domain.Currency;
import com.github.stephenenright.walletwatchlist.web.api.asset.repository.BlockChainRepository;
import com.github.stephenenright.walletwatchlist.web.api.asset.repository.BlockchainAssetRepository;
import com.github.stephenenright.walletwatchlist.web.api.asset.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AssetIntegrationTestHelper {

	private final BlockChainRepository blockChainRepository;
	private final CurrencyRepository currencyRepository;
	private final BlockchainAssetRepository blockchainAssetRepository;

	private static final String USDC_ETH_CONTRACT = "0xA0b86991c6218b36c1d19D4a2e9Eb0cE3606eB48";
	private static final String USDT_ETH_CONTRACT = "0xdAC17F958D2ee523a2206206994597C13D831ec7";
	private static final String DAI_ETH_CONTRACT = "0x6B175474E89094C44Da98b954EedeeCB5BE5f855";
	private static final String WBTC_ETH_CONTRACT = "0x2260FAC5E5542a773Aa44fBCfeDf7C193bc2C599";

	public AssetFixtureResult create(AssetFixtureSettings settings) {
		var blockChains = loadBlockChains(settings.blockChainSettings());

		CurrencyFixtureResult currencies = null;
		if (settings.currencySettings() != null) {
			currencies = loadCurrencies(settings.currencySettings());
		}

		BlockchainAssetFixtureResult blockchainAssets = null;
		if (settings.blockchainAssetSettings() != null) {
			blockchainAssets = loadBlockchainAssets(settings.blockchainAssetSettings(), blockChains);
		}

		return AssetFixtureResult.builder().blockChains(blockChains).currencies(currencies)
				.blockchainAssets(blockchainAssets).build();
	}

	private BlockChainFixtureResult loadBlockChains(BlockChainFixtureSettings settings) {
		var builder = BlockChainFixtureResult.builder();

		if (settings.createEthereum()) {
			builder.ethereum(blockChainRepository.findByCode("ETHEREUM").orElse(null));
		}
		if (settings.createBitcoin()) {
			builder.bitcoin(blockChainRepository.findByCode("BITCOIN").orElse(null));
		}
		if (settings.createArbitrum()) {
			builder.arbitrum(blockChainRepository.findByCode("ARBITRUM").orElse(null));
		}
		if (settings.createOptimism()) {
			builder.optimism(blockChainRepository.findByCode("OPTIMISM").orElse(null));
		}
		if (settings.createBase()) {
			builder.base(blockChainRepository.findByCode("BASE").orElse(null));
		}

		return builder.build();
	}

	private CurrencyFixtureResult loadCurrencies(CurrencyFixtureSettings settings) {
		var builder = CurrencyFixtureResult.builder();

		if (settings.createBTC()) {
			builder.btc(currencyRepository.findBySymbol("BTC").orElse(null));
		}
		if (settings.createETH()) {
			builder.eth(currencyRepository.findBySymbol("ETH").orElse(null));
		}
		if (settings.createUSDC()) {
			builder.usdc(currencyRepository.findBySymbol("USDC").orElse(null));
		}
		if (settings.createUSDT()) {
			builder.usdt(currencyRepository.findBySymbol("USDT").orElse(null));
		}
		if (settings.createDAI()) {
			builder.dai(currencyRepository.findBySymbol("DAI").orElse(null));
		}
		if (settings.createWBTC()) {
			builder.wbtc(currencyRepository.findBySymbol("WBTC").orElse(null));
		}

		return builder.build();
	}

	private BlockchainAssetFixtureResult loadBlockchainAssets(BlockchainAssetFixtureSettings settings,
			BlockChainFixtureResult blockChains) {
		var builder = BlockchainAssetFixtureResult.builder();

		if (settings.createBtcOnBitcoin() && blockChains.bitcoin() != null) {
			builder.btcOnBitcoin(blockchainAssetRepository
					.findByBlockChainIdAndIsNativeTrue(blockChains.bitcoin().getId()).orElse(null));
		}
		if (settings.createEthOnEthereum() && blockChains.ethereum() != null) {
			builder.ethOnEthereum(blockchainAssetRepository
					.findByBlockChainIdAndIsNativeTrue(blockChains.ethereum().getId()).orElse(null));
		}
		if (settings.createUsdcOnEthereum() && blockChains.ethereum() != null) {
			builder.usdcOnEthereum(blockchainAssetRepository
					.findByContractAddressIgnoreCaseAndBlockChainId(USDC_ETH_CONTRACT, blockChains.ethereum().getId())
					.orElse(null));
		}
		if (settings.createUsdtOnEthereum() && blockChains.ethereum() != null) {
			builder.usdtOnEthereum(blockchainAssetRepository
					.findByContractAddressIgnoreCaseAndBlockChainId(USDT_ETH_CONTRACT, blockChains.ethereum().getId())
					.orElse(null));
		}
		if (settings.createDaiOnEthereum() && blockChains.ethereum() != null) {
			builder.daiOnEthereum(blockchainAssetRepository
					.findByContractAddressIgnoreCaseAndBlockChainId(DAI_ETH_CONTRACT, blockChains.ethereum().getId())
					.orElse(null));
		}
		if (settings.createWbtcOnEthereum() && blockChains.ethereum() != null) {
			builder.wbtcOnEthereum(blockchainAssetRepository
					.findByContractAddressIgnoreCaseAndBlockChainId(WBTC_ETH_CONTRACT, blockChains.ethereum().getId())
					.orElse(null));
		}

		return builder.build();
	}
}
