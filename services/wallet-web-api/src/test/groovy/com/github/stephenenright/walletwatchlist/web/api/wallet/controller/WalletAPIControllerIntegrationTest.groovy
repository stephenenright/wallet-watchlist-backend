/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.wallet.controller

import com.github.stephenenright.walletwatchlist.web.api.asset.domain.BlockChain
import com.github.stephenenright.walletwatchlist.web.api.asset.fixture.BlockChainFixtureHelper
import com.github.stephenenright.walletwatchlist.web.api.asset.repository.BlockChainRepository
import com.github.stephenenright.walletwatchlist.web.api.common.test.BaseApiControllerTest
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WalletStatus
import com.github.stephenenright.walletwatchlist.web.api.wallet.fixture.WalletFixtureHelper
import com.github.stephenenright.walletwatchlist.web.api.wallet.fixture.integration.WalletFixtureResult
import com.github.stephenenright.walletwatchlist.web.api.wallet.fixture.integration.WalletFixtureSettings
import com.github.stephenenright.walletwatchlist.web.api.wallet.fixture.integration.WalletIntegrationTestHelper
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WatchedWallet
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WatchedWalletStatus
import com.github.stephenenright.walletwatchlist.web.api.wallet.repository.WalletActivityRepository
import com.github.stephenenright.walletwatchlist.web.api.wallet.repository.WalletAssetRepository
import com.github.stephenenright.walletwatchlist.web.api.wallet.repository.WalletRepository
import com.github.stephenenright.walletwatchlist.web.api.wallet.repository.WatchedWalletRepository
import com.github.stephenenright.walletwatchlist.web.api.user.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.transaction.annotation.Transactional

import static org.hamcrest.Matchers.*
import static org.hamcrest.number.IsCloseTo.closeTo
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@Transactional
class WalletAPIControllerIntegrationTest extends BaseApiControllerTest {

	@Autowired
	WalletRepository walletRepository

	@Autowired
	WalletAssetRepository walletAssetRepository

	@Autowired
	WalletActivityRepository walletActivityRepository

	@Autowired
	BlockChainRepository blockChainRepository

	@Autowired
	WatchedWalletRepository watchedWalletRepository

	@Autowired
	UserRepository userRepository

	@Autowired
	WalletIntegrationTestHelper walletTestHelper

	WalletFixtureResult fixtures

	def setup() {
		fixtures = walletTestHelper.create(WalletFixtureSettings.builder().build())
	}

	def "list wallets returns paged results with balanceUsd"() {
		when:
		def result = mockMvc.perform(get("/api/wallets")
				.contentType(MediaType.APPLICATION_JSON))

		then:
		result.andExpect(status().isOk())
				.andExpect(jsonPath('$.status', is("SUCCESS")))
				.andExpect(jsonPath('$.data.results', hasSize(greaterThanOrEqualTo(3))))
				.andExpect(jsonPath('$.data.pageNumber', is(1)))
				.andExpect(jsonPath('$.data.totalResults', greaterThanOrEqualTo(3)))
				.andExpect(jsonPath('$.data.results[*].id', everyItem(notNullValue())))
				.andExpect(jsonPath('$.data.results[*].address', everyItem(notNullValue())))
				// Verify balanceUsd field exists on results (some may be null for wallets without assets)
				.andExpect(jsonPath('$.data.results[0].balanceUsd').exists())
	}

	def "list wallets filters by status"() {
		given:
		def blockChain = fixtures.assets().blockChains().ethereum()
		def dormantWallet = walletRepository.save(WalletFixtureHelper.createWallet("0xdormant3333333333333333333333333333333333", blockChain, WalletStatus.DORMANT))

		when:
		def result = mockMvc.perform(get("/api/wallets")
				.param("status", "DORMANT")
				.contentType(MediaType.APPLICATION_JSON))

		then:
		result.andExpect(status().isOk())
				.andExpect(jsonPath('$.status', is("SUCCESS")))
				.andExpect(jsonPath('$.data.results[*].status', everyItem(is("DORMANT"))))
				.andExpect(jsonPath('$.data.results[*].id', hasItem(dormantWallet.id.toString())))
	}

	def "get wallet by id returns wallet detail with balances and activity"() {
		when:
		def result = mockMvc.perform(get("/api/wallets/{id}", fixtures.ethWallet1().id))

		then:
		result.andExpect(status().isOk())
				.andExpect(jsonPath('$.status', is("SUCCESS")))
				.andExpect(jsonPath('$.data.id', is(fixtures.ethWallet1().id.toString())))
				.andExpect(jsonPath('$.data.address', is(fixtures.ethWallet1().address)))
				.andExpect(jsonPath('$.data.blockChainCode', is("ETHEREUM")))
				.andExpect(jsonPath('$.data.status', is("ACTIVE")))
				.andExpect(jsonPath('$.data.syncStatus', is("SYNCED")))
				.andExpect(jsonPath('$.data.dateCreated', notNullValue()))
				.andExpect(jsonPath('$.data.balanceUsd', notNullValue()))
				.andExpect(jsonPath('$.data.assets', hasSize(2)))
				// ETH asset: quantity 2.5
				.andExpect(jsonPath('$.data.assets[?(@.currencySymbol == "ETH")].quantity', hasItem(closeTo(2.5, 0.01))))
				// USDC asset: quantity 1500
				.andExpect(jsonPath('$.data.assets[?(@.currencySymbol == "USDC")].quantity', hasItem(closeTo(1500.0, 0.01))))
				.andExpect(jsonPath('$.data.recentActivity', hasSize(3)))
				.andExpect(jsonPath('$.data.recentActivity[0].activityType', notNullValue()))
				.andExpect(jsonPath('$.data.recentActivity[0].summary', notNullValue()))
				.andExpect(jsonPath('$.data.recentActivity[0].valueUsd', notNullValue()))
	}

	def "get wallet by id returns wallet with empty balances and activity when none exist"() {
		when:
		def result = mockMvc.perform(get("/api/wallets/{id}", fixtures.ethWallet2().id))

		then:
		result.andExpect(status().isOk())
				.andExpect(jsonPath('$.status', is("SUCCESS")))
				.andExpect(jsonPath('$.data.id', is(fixtures.ethWallet2().id.toString())))
				.andExpect(jsonPath('$.data.balanceUsd', is(0)))
				.andExpect(jsonPath('$.data.assets', hasSize(0)))
				.andExpect(jsonPath('$.data.recentActivity', hasSize(0)))
	}

	def "get wallet by id returns not found for unknown id"() {
		when:
		def result = mockMvc.perform(get("/api/wallets/{id}", UUID.randomUUID()))

		then:
		result.andExpect(status().isNotFound())
				.andExpect(jsonPath('$.status', is("NOT_FOUND")))
	}

	def "create wallet successfully creates wallet with mocked data from existing wallet"() {
		given:
		def blockChain = fixtures.assets().blockChains().ethereum()
		def requestBody = """
			{
				"address": "0xcreate55555555555555555555555555555555555",
				"blockChainId": "${blockChain.id}"
			}
		"""

		when:
		def result = mockMvc.perform(post("/api/wallets")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))

		then:
		result.andExpect(status().isCreated())
				.andExpect(jsonPath('$.status', is("SUCCESS")))
				.andExpect(jsonPath('$.data.id', notNullValue()))
				.andExpect(jsonPath('$.data.address', is("0xcreate55555555555555555555555555555555555")))
				.andExpect(jsonPath('$.data.blockChainCode', is("ETHEREUM")))
				.andExpect(jsonPath('$.data.status', is("ACTIVE")))
				.andExpect(jsonPath('$.data.syncStatus', is("SYNCED")))
				.andExpect(jsonPath('$.data.dateCreated', notNullValue()))
				// Mocked data should be copied from existing wallet on same blockchain
				.andExpect(jsonPath('$.data.balanceUsd').exists())
				.andExpect(jsonPath('$.data.assets', hasSize(greaterThanOrEqualTo(1))))
				.andExpect(jsonPath('$.data.assets[0].currencySymbol', notNullValue()))
				.andExpect(jsonPath('$.data.assets[0].quantity', notNullValue()))
	}

	def "create wallet fails with blank address"() {
		given:
		def blockChain = fixtures.assets().blockChains().ethereum()
		def requestBody = """
			{
				"address": "",
				"blockChainId": "${blockChain.id}"
			}
		"""

		when:
		def result = mockMvc.perform(post("/api/wallets")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))

		then:
		result.andExpect(status().isBadRequest())
	}

	def "create wallet fails when blockchain does not exist"() {
		given:
		def requestBody = """
			{
				"address": "0xbadchain66666666666666666666666666666666",
				"blockChainId": "${UUID.randomUUID()}"
			}
		"""

		when:
		def result = mockMvc.perform(post("/api/wallets")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))

		then:
		result.andExpect(status().isBadRequest())
				.andExpect(jsonPath('$.error.validationErrors.blockChainId', notNullValue()))
	}

	def "update wallet updates status successfully"() {
		given:
		def requestBody = """
			{
				"status": "DORMANT"
			}
		"""

		when:
		def result = mockMvc.perform(put("/api/wallets/{id}", fixtures.ethWallet2().id)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))

		then:
		result.andExpect(status().isOk())
				.andExpect(jsonPath('$.status', is("SUCCESS")))
				.andExpect(jsonPath('$.data.status', is("DORMANT")))
				.andExpect(jsonPath('$.data.assets', notNullValue()))
				.andExpect(jsonPath('$.data.recentActivity', notNullValue()))
	}

	def "update wallet returns not found for unknown id"() {
		given:
		def requestBody = """
			{
				"status": "DORMANT"
			}
		"""

		when:
		def result = mockMvc.perform(put("/api/wallets/{id}", UUID.randomUUID())
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))

		then:
		result.andExpect(status().isNotFound())
				.andExpect(jsonPath('$.status', is("NOT_FOUND")))
	}

	def "delete wallet removes wallet successfully when no watchers"() {
		given:
		def blockChain = fixtures.assets().blockChains().ethereum()
		def wallet = walletRepository.save(WalletFixtureHelper.createWallet("0xdelete88888888888888888888888888888888888", blockChain))
		def walletId = wallet.id

		when:
		def result = mockMvc.perform(delete("/api/wallets/{id}", walletId))

		then:
		result.andExpect(status().isOk())
				.andExpect(jsonPath('$.status', is("SUCCESS")))

		and:
		walletRepository.findById(walletId).isEmpty()
	}

	def "delete wallet fails when wallet has active watchers"() {
		given:
		def user = userRepository.findAll().first()
		watchedWalletRepository.save(WatchedWallet.builder()
				.watcher(user)
				.wallet(fixtures.ethWallet1())
				.label("Test Watcher")
				.status(WatchedWalletStatus.ACTIVE)
				.build())

		when:
		def result = mockMvc.perform(delete("/api/wallets/{id}", fixtures.ethWallet1().id))

		then:
		result.andExpect(status().isBadRequest())
				.andExpect(jsonPath('$.status', is("ERROR")))
	}

	def "delete wallet returns not found for unknown id"() {
		when:
		def result = mockMvc.perform(delete("/api/wallets/{id}", UUID.randomUUID()))

		then:
		result.andExpect(status().isNotFound())
				.andExpect(jsonPath('$.status', is("NOT_FOUND")))
	}
}
