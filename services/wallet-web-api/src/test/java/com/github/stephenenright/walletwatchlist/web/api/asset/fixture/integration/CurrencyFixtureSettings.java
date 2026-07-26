package com.github.stephenenright.walletwatchlist.web.api.asset.fixture.integration;

import lombok.Builder;

@Builder(toBuilder = true)
public record CurrencyFixtureSettings(boolean createBTC, boolean createETH, boolean createUSDC, boolean createUSDT,
		boolean createDAI, boolean createWBTC) {

	public static CurrencyFixtureSettingsBuilder builder() {
		return new CurrencyFixtureSettingsBuilder().createBTC(true).createETH(true).createUSDC(true).createUSDT(true)
				.createDAI(true).createWBTC(true);
	}
}
