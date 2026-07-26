/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.common.test.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

	@Bean
	@ServiceConnection
	PostgreSQLContainer postgresContainer() {
		def imageName = DockerImageName.parse("postgres:16-alpine")
		new PostgreSQLContainer(imageName)
				.withReuse(true)
	}
}
