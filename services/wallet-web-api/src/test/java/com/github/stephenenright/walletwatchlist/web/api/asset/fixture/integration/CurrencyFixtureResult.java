package com.github.stephenenright.walletwatchlist.web.api.asset.fixture.integration;

import com.github.stephenenright.walletwatchlist.web.api.asset.domain.Currency;
import lombok.Builder;

@Builder(toBuilder = true)
public record CurrencyFixtureResult(Currency btc, Currency eth, Currency usdc, Currency usdt, Currency dai,
		Currency wbtc) {
}
