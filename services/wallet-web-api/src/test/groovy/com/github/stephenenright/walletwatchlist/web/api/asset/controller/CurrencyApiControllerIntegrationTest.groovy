/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.asset.controller

import com.github.stephenenright.walletwatchlist.web.api.asset.fixture.integration.AssetFixtureResult
import com.github.stephenenright.walletwatchlist.web.api.asset.fixture.integration.AssetFixtureSettings
import com.github.stephenenright.walletwatchlist.web.api.asset.fixture.integration.AssetIntegrationTestHelper
import com.github.stephenenright.walletwatchlist.web.api.asset.fixture.integration.BlockChainFixtureSettings
import com.github.stephenenright.walletwatchlist.web.api.asset.fixture.integration.CurrencyFixtureSettings
import com.github.stephenenright.walletwatchlist.web.api.common.test.BaseApiControllerTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.transaction.annotation.Transactional

import static org.hamcrest.Matchers.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@Transactional
class CurrencyApiControllerIntegrationTest extends BaseApiControllerTest {

	@Autowired
	AssetIntegrationTestHelper assetTestHelper

	AssetFixtureResult fixtures

	def setup() {
		fixtures = assetTestHelper.create(
				AssetFixtureSettings.builder()
				.blockChainSettings(BlockChainFixtureSettings.builder().createEthereum(true).build())
				.currencySettings(CurrencyFixtureSettings.builder()
				.createBTC(true)
				.createETH(true)
				.createUSDC(true)
				.build())
				.build())
	}

	def "list currencies returns paged results with all fields"() {
		when:
		def result = mockMvc.perform(get("/api/currencies")
				.contentType(MediaType.APPLICATION_JSON))

		then:
		result.andExpect(status().isOk())
				.andExpect(jsonPath('$.status', is("SUCCESS")))
				.andExpect(jsonPath('$.data.results', hasSize(greaterThanOrEqualTo(3))))
				.andExpect(jsonPath('$.data.pageNumber', is(1)))
				.andExpect(jsonPath('$.data.totalResults', greaterThanOrEqualTo(3)))
				.andExpect(jsonPath('$.data.results[*].symbol', hasItems("BTC", "ETH", "USDC")))
				.andExpect(jsonPath('$.data.results[*].id', everyItem(notNullValue())))
				.andExpect(jsonPath('$.data.results[*].dateCreated', everyItem(notNullValue())))
	}

	def "list currencies supports pagination"() {
		when:
		def result = mockMvc.perform(get("/api/currencies")
				.param("page", "1")
				.param("size", "2"))

		then:
		result.andExpect(status().isOk())
				.andExpect(jsonPath('$.status', is("SUCCESS")))
				.andExpect(jsonPath('$.data.results', hasSize(2)))
				.andExpect(jsonPath('$.data.pageNumber', is(1)))
				.andExpect(jsonPath('$.data.pageSize', is(2)))
				.andExpect(jsonPath('$.data.totalResults', greaterThanOrEqualTo(3)))
				.andExpect(jsonPath('$.data.totalPages', greaterThanOrEqualTo(2)))
	}

	def "get by symbol returns currency with all fields"() {
		when:
		def result = mockMvc.perform(get("/api/currencies/{symbol}", "ETH"))

		then:
		result.andExpect(status().isOk())
				.andExpect(jsonPath('$.status', is("SUCCESS")))
				.andExpect(jsonPath('$.data.id', is(fixtures.currencies().eth().id.toString())))
				.andExpect(jsonPath('$.data.symbol', is("ETH")))
				.andExpect(jsonPath('$.data.name', is("Ethereum")))
				.andExpect(jsonPath('$.data.dateCreated', notNullValue()))
	}

	def "get by symbol returns not found for unknown symbol"() {
		when:
		def result = mockMvc.perform(get("/api/currencies/{symbol}", "UNKNOWN"))

		then:
		result.andExpect(status().isNotFound())
				.andExpect(jsonPath('$.status', is("NOT_FOUND")))
	}
}
