/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.config;

import javax.sql.DataSource;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.flyway.FlywayConfigurationCustomizer;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DatabaseConfig {

	@Configuration
	@ConditionalOnProperty(name = "app.database.type", havingValue = "postgres", matchIfMissing = true)
	static class PostgresConfig {

		@Bean
		@Primary
		@ConditionalOnMissingBean(DataSource.class)
		@ConfigurationProperties("app.database.postgres")
		public DataSourceProperties postgresDataSourceProperties() {
			return new DataSourceProperties();
		}

		@Bean
		@Primary
		@ConditionalOnMissingBean(DataSource.class)
		public DataSource dataSource(DataSourceProperties postgresDataSourceProperties) {
			return postgresDataSourceProperties.initializeDataSourceBuilder().build();
		}

		@Bean
		@ConditionalOnMissingBean(FlywayConfigurationCustomizer.class)
		public FlywayConfigurationCustomizer flywayConfigurationCustomizer() {
			return (FluentConfiguration config) -> config.locations("classpath:db/migration/main/master");
		}
	}

	@Configuration
	@ConditionalOnProperty(name = "app.database.type", havingValue = "h2")
	static class H2Config {

		@Bean
		@Primary
		@ConditionalOnMissingBean(DataSource.class)
		@ConfigurationProperties("app.database.h2")
		public DataSourceProperties h2DataSourceProperties() {
			return new DataSourceProperties();
		}

		@Bean
		@Primary
		@ConditionalOnMissingBean(DataSource.class)
		public DataSource dataSource(DataSourceProperties h2DataSourceProperties) {
			return h2DataSourceProperties.initializeDataSourceBuilder().build();
		}

		@Bean
		@ConditionalOnMissingBean(FlywayConfigurationCustomizer.class)
		public FlywayConfigurationCustomizer flywayConfigurationCustomizer() {
			return (FluentConfiguration config) -> config.locations("classpath:db/migration/main/master");
		}
	}
}
