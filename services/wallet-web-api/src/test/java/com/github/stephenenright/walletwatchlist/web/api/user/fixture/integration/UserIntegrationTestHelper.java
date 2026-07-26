package com.github.stephenenright.walletwatchlist.web.api.user.fixture.integration;

import com.github.stephenenright.walletwatchlist.web.api.common.jpa.JpaHelper;
import com.github.stephenenright.walletwatchlist.web.api.user.domain.User;
import com.github.stephenenright.walletwatchlist.web.api.user.fixture.UserFixtureHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserIntegrationTestHelper {
	private final JpaHelper jpaHelper;

	public UserFixtureResult create(UserFixtureSettings settings) {
		var fixture = UserFixtureResult.builder().build();
		fixture = createJohnDoe(fixture, settings);
		fixture = createJaneDoe(fixture, settings);
		fixture = createBobSmith(fixture, settings);
		fixture = createAliceJohnson(fixture, settings);
		return fixture;
	}

	private UserFixtureResult createJohnDoe(UserFixtureResult fixture, UserFixtureSettings settings) {
		if (!settings.createJohnDoe()) {
			return fixture;
		}
		User user = jpaHelper.save(UserFixtureHelper.createJohnDoe(), User.class);
		return fixture.toBuilder().johnDoe(user).build();
	}

	private UserFixtureResult createJaneDoe(UserFixtureResult fixture, UserFixtureSettings settings) {
		if (!settings.createJaneDoe()) {
			return fixture;
		}
		User user = jpaHelper.save(UserFixtureHelper.createJaneDoe(), User.class);
		return fixture.toBuilder().janeDoe(user).build();
	}

	private UserFixtureResult createBobSmith(UserFixtureResult fixture, UserFixtureSettings settings) {
		if (!settings.createBobSmith()) {
			return fixture;
		}
		User user = jpaHelper.save(UserFixtureHelper.createBobSmith(), User.class);
		return fixture.toBuilder().bobSmith(user).build();
	}

	private UserFixtureResult createAliceJohnson(UserFixtureResult fixture, UserFixtureSettings settings) {
		if (!settings.createAliceJohnson()) {
			return fixture;
		}
		User user = jpaHelper.save(UserFixtureHelper.createAliceJohnson(), User.class);
		return fixture.toBuilder().aliceJohnson(user).build();
	}
}
