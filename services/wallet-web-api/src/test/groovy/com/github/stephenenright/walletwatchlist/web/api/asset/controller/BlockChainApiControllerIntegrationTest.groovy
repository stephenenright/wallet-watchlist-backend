/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.asset.controller

import com.github.stephenenright.walletwatchlist.web.api.asset.fixture.integration.AssetFixtureResult
import com.github.stephenenright.walletwatchlist.web.api.asset.fixture.integration.AssetFixtureSettings
import com.github.stephenenright.walletwatchlist.web.api.asset.fixture.integration.AssetIntegrationTestHelper
import com.github.stephenenright.walletwatchlist.web.api.asset.fixture.integration.BlockChainFixtureSettings
import com.github.stephenenright.walletwatchlist.web.api.common.test.BaseApiControllerTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.transaction.annotation.Transactional

import static org.hamcrest.Matchers.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@Transactional
class BlockChainApiControllerIntegrationTest extends BaseApiControllerTest {

	@Autowired
	AssetIntegrationTestHelper assetTestHelper

	AssetFixtureResult fixtures

	def setup() {
		fixtures = assetTestHelper.create(
				AssetFixtureSettings.builder()
				.blockChainSettings(BlockChainFixtureSettings.builder()
				.createEthereum(true)
				.createBitcoin(true)
				.createArbitrum(true)
				.build())
				.build())
	}

	def "list blockchains returns all blockchains with all fields"() {
		when:
		def result = mockMvc.perform(get("/api/blockchains")
				.contentType(MediaType.APPLICATION_JSON))

		then:
		result.andExpect(status().isOk())
				.andExpect(jsonPath('$.status', is("SUCCESS")))
				.andExpect(jsonPath('$.data.results', hasSize(greaterThanOrEqualTo(3))))
				.andExpect(jsonPath('$.data.results[*].code', hasItems("ETHEREUM", "BITCOIN", "ARBITRUM")))
				.andExpect(jsonPath('$.data.results[*].mainnet', everyItem(is("mainnet"))))
				.andExpect(jsonPath('$.data.results[*].id', everyItem(notNullValue())))
				.andExpect(jsonPath('$.data.results[*].dateCreated', everyItem(notNullValue())))
	}

	def "get by id returns blockchain with all fields"() {
		when:
		def result = mockMvc.perform(get("/api/blockchains/{id}", fixtures.blockChains().ethereum().id))

		then:
		result.andExpect(status().isOk())
				.andExpect(jsonPath('$.status', is("SUCCESS")))
				.andExpect(jsonPath('$.data.id', is(fixtures.blockChains().ethereum().id.toString())))
				.andExpect(jsonPath('$.data.code', is("ETHEREUM")))
				.andExpect(jsonPath('$.data.name', is("Ethereum")))
				.andExpect(jsonPath('$.data.mainnet', is("mainnet")))
				.andExpect(jsonPath('$.data.nativeCurrency', is("ETH")))
				.andExpect(jsonPath('$.data.dateCreated', notNullValue()))
	}

	def "get by id returns not found for unknown id"() {
		when:
		def result = mockMvc.perform(get("/api/blockchains/{id}", UUID.randomUUID()))

		then:
		result.andExpect(status().isNotFound())
				.andExpect(jsonPath('$.status', is("NOT_FOUND")))
	}
}
