package com.github.stephenenright.walletwatchlist.web.api.user.fixture.integration;

import com.github.stephenenright.walletwatchlist.web.api.user.domain.User;
import lombok.Builder;

@Builder(toBuilder = true)
public record UserFixtureResult(User johnDoe, User janeDoe, User bobSmith, User aliceJohnson) {
}
