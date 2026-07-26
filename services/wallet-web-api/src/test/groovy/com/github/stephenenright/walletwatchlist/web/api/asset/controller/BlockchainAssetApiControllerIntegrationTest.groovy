/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.asset.controller

import com.github.stephenenright.walletwatchlist.web.api.asset.fixture.integration.AssetFixtureResult
import com.github.stephenenright.walletwatchlist.web.api.asset.fixture.integration.AssetFixtureSettings
import com.github.stephenenright.walletwatchlist.web.api.asset.fixture.integration.AssetIntegrationTestHelper
import com.github.stephenenright.walletwatchlist.web.api.asset.fixture.integration.BlockChainFixtureSettings
import com.github.stephenenright.walletwatchlist.web.api.asset.fixture.integration.BlockchainAssetFixtureSettings
import com.github.stephenenright.walletwatchlist.web.api.asset.fixture.integration.CurrencyFixtureSettings
import com.github.stephenenright.walletwatchlist.web.api.common.test.BaseApiControllerTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.transaction.annotation.Transactional

import static org.hamcrest.Matchers.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@Transactional
class BlockchainAssetApiControllerIntegrationTest extends BaseApiControllerTest {

	static final String USDC_ETH_CONTRACT = "0xA0b86991c6218b36c1d19D4a2e9Eb0cE3606eB48"

	@Autowired
	AssetIntegrationTestHelper assetTestHelper

	AssetFixtureResult fixtures

	def setup() {
		fixtures = assetTestHelper.create(
				AssetFixtureSettings.builder()
				.blockChainSettings(BlockChainFixtureSettings.builder()
				.createEthereum(true)
				.createBitcoin(true)
				.build())
				.currencySettings(CurrencyFixtureSettings.builder()
				.createETH(true)
				.createUSDC(true)
				.createUSDT(true)
				.createBTC(true)
				.build())
				.blockchainAssetSettings(BlockchainAssetFixtureSettings.builder()
				.createEthOnEthereum(true)
				.createUsdcOnEthereum(true)
				.createUsdtOnEthereum(true)
				.createBtcOnBitcoin(true)
				.build())
				.build())
	}

	def "list blockchain assets returns all assets with all fields"() {
		when:
		def result = mockMvc.perform(get("/api/blockchain-assets")
				.contentType(MediaType.APPLICATION_JSON))

		then:
		result.andExpect(status().isOk())
				.andExpect(jsonPath('$.status', is("SUCCESS")))
				.andExpect(jsonPath('$.data.results', hasSize(greaterThanOrEqualTo(4))))
				.andExpect(jsonPath('$.data.results[*].id', everyItem(notNullValue())))
				.andExpect(jsonPath('$.data.results[*].dateCreated', everyItem(notNullValue())))
				.andExpect(jsonPath('$.data.results[*].currency', everyItem(notNullValue())))
				.andExpect(jsonPath('$.data.results[*].blockChain', everyItem(notNullValue())))
	}

	def "get by id returns native blockchain asset with all fields"() {
		when:
		def result = mockMvc.perform(get("/api/blockchain-assets/{id}", fixtures.blockchainAssets().ethOnEthereum().id))

		then:
		result.andExpect(status().isOk())
				.andExpect(jsonPath('$.status', is("SUCCESS")))
				.andExpect(jsonPath('$.data.id', is(fixtures.blockchainAssets().ethOnEthereum().id.toString())))
				.andExpect(jsonPath('$.data.nativeAsset', is(true)))
				.andExpect(jsonPath('$.data.contractAddress', nullValue()))
				.andExpect(jsonPath('$.data.dateCreated', notNullValue()))
				.andExpect(jsonPath('$.data.currency.id', is(fixtures.currencies().eth().id.toString())))
				.andExpect(jsonPath('$.data.currency.symbol', is("ETH")))
				.andExpect(jsonPath('$.data.currency.name', is("Ethereum")))
				.andExpect(jsonPath('$.data.blockChain.id', is(fixtures.blockChains().ethereum().id.toString())))
				.andExpect(jsonPath('$.data.blockChain.code', is("ETHEREUM")))
				.andExpect(jsonPath('$.data.blockChain.name', is("Ethereum")))
	}

	def "get by id returns token blockchain asset with all fields"() {
		when:
		def result = mockMvc.perform(get("/api/blockchain-assets/{id}", fixtures.blockchainAssets().usdcOnEthereum().id))

		then:
		result.andExpect(status().isOk())
				.andExpect(jsonPath('$.status', is("SUCCESS")))
				.andExpect(jsonPath('$.data.id', is(fixtures.blockchainAssets().usdcOnEthereum().id.toString())))
				.andExpect(jsonPath('$.data.nativeAsset', is(false)))
				.andExpect(jsonPath('$.data.contractAddress', is(USDC_ETH_CONTRACT)))
				.andExpect(jsonPath('$.data.dateCreated', notNullValue()))
				.andExpect(jsonPath('$.data.currency.id', is(fixtures.currencies().usdc().id.toString())))
				.andExpect(jsonPath('$.data.currency.symbol', is("USDC")))
				.andExpect(jsonPath('$.data.currency.name', is("USD Coin")))
				.andExpect(jsonPath('$.data.blockChain.id', is(fixtures.blockChains().ethereum().id.toString())))
				.andExpect(jsonPath('$.data.blockChain.code', is("ETHEREUM")))
	}

	def "get by id returns not found for unknown id"() {
		when:
		def result = mockMvc.perform(get("/api/blockchain-assets/{id}", UUID.randomUUID()))

		then:
		result.andExpect(status().isNotFound())
				.andExpect(jsonPath('$.status', is("NOT_FOUND")))
	}
}
