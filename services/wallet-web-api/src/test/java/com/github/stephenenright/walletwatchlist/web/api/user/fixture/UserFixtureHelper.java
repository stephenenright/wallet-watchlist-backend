package com.github.stephenenright.walletwatchlist.web.api.user.fixture;

import com.github.stephenenright.walletwatchlist.web.api.user.domain.User;
import java.time.Instant;

public final class UserFixtureHelper {

	private UserFixtureHelper() {
	}

	public static User createJohnDoe() {
		return createUser("John", "Doe", "john.doe.test@example.com");
	}

	public static User createJaneDoe() {
		return createUser("Jane", "Doe", "jane.doe.test@example.com");
	}

	public static User createBobSmith() {
		return createUser("Bob", "Smith", "bob.smith.test@example.com");
	}

	public static User createAliceJohnson() {
		return createUser("Alice", "Johnson", "alice.johnson.test@example.com");
	}

	public static User createUser(String firstName, String lastName, String email) {
		return User.builder().firstName(firstName).lastName(lastName).email(email).dateCreated(Instant.now())
				.dateUpdated(Instant.now()).build();
	}
}
