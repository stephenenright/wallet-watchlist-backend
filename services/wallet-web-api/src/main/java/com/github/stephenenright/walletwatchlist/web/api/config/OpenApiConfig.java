/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.tags.Tag;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI walletWatchlistOpenAPI() {
		return new OpenAPI().info(new Info().title("Wallet Watchlist API").description(
				"API for managing blockchain wallet watchlists. Enables users to track wallets across multiple blockchains, view balances, and monitor activity.")
				.version("1.0.0").contact(new Contact().name("Wallet Watchlist Team"))
				.license(new License().name("MIT")))
				.tags(List.of(new Tag().name("Wallets").description(
						"Manage blockchain wallets - create, update, delete, and view wallet details including balances and activity"),
						new Tag().name("Watched Wallets")
								.description("Manage user watchlists - link users to wallets they want to monitor"),
						new Tag().name("Blockchains").description("View supported blockchain networks"),
						new Tag().name("Blockchain Assets")
								.description("View blockchain assets (native currencies and tokens)"),
						new Tag().name("Currencies").description("View supported currencies"),
						new Tag().name("Users").description("User management")));
	}
}
