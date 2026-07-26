/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.common.test

import com.github.stephenenright.walletwatchlist.web.api.WalletWebApiApplication
import com.github.stephenenright.walletwatchlist.web.api.common.test.config.TestcontainersConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

@ActiveProfiles("test")
@Import(TestcontainersConfiguration)
@SpringBootTest(classes = WalletWebApiApplication)
@AutoConfigureMockMvc
abstract class BaseApiControllerTest extends Specification {

	@Autowired
	MockMvc mockMvc
}
