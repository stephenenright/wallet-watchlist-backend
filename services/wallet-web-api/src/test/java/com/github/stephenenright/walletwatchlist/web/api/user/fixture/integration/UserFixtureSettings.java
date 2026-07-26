package com.github.stephenenright.walletwatchlist.web.api.user.fixture.integration;

import lombok.Builder;

@Builder(toBuilder = true)
public record UserFixtureSettings(boolean createJohnDoe, boolean createJaneDoe, boolean createBobSmith,
		boolean createAliceJohnson) {

	public static UserFixtureSettingsBuilder builder() {
		return new UserFixtureSettingsBuilder().createJohnDoe(true).createJaneDoe(true).createBobSmith(true)
				.createAliceJohnson(true);
	}
}
