/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.common.test

import com.github.stephenenright.walletwatchlist.web.api.WalletWebApiApplication
import com.github.stephenenright.walletwatchlist.web.api.common.test.config.TestcontainersConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@ActiveProfiles("test")
@Import(TestcontainersConfiguration)
@SpringBootTest(classes = WalletWebApiApplication, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class BaseAPIIntegrationTest extends Specification {
}
