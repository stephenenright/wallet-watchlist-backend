package com.github.stephenenright.walletwatchlist.web.api.asset.fixture;

import com.github.stephenenright.walletwatchlist.web.api.asset.domain.Currency;
import java.time.Instant;

public final class CurrencyFixtureHelper {

	private CurrencyFixtureHelper() {
	}

	public static Currency createBtc() {
		return createCurrency("Bitcoin", "BTC");
	}

	public static Currency createEth() {
		return createCurrency("Ethereum", "ETH");
	}

	public static Currency createUsdc() {
		return createCurrency("USD Coin", "USDC");
	}

	public static Currency createUsdt() {
		return createCurrency("Tether", "USDT");
	}

	public static Currency createDai() {
		return createCurrency("Dai Stablecoin", "DAI");
	}

	public static Currency createWbtc() {
		return createCurrency("Wrapped Bitcoin", "WBTC");
	}

	public static Currency createCurrency(String name, String symbol) {
		return Currency.builder().name(name).symbol(symbol).dateCreated(Instant.now()).dateUpdated(Instant.now())
				.build();
	}
}
