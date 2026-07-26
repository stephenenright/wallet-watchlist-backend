/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.wallet.controller

import com.github.stephenenright.walletwatchlist.web.api.asset.domain.BlockChain
import com.github.stephenenright.walletwatchlist.web.api.asset.fixture.BlockChainFixtureHelper
import com.github.stephenenright.walletwatchlist.web.api.asset.repository.BlockChainRepository
import com.github.stephenenright.walletwatchlist.web.api.common.test.BaseApiControllerTest
import com.github.stephenenright.walletwatchlist.web.api.user.domain.User
import com.github.stephenenright.walletwatchlist.web.api.user.fixture.UserFixtureHelper
import com.github.stephenenright.walletwatchlist.web.api.user.repository.UserRepository
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.Wallet
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WatchedWalletStatus
import com.github.stephenenright.walletwatchlist.web.api.wallet.fixture.WalletFixtureHelper
import com.github.stephenenright.walletwatchlist.web.api.wallet.fixture.WatchedWalletFixtureHelper
import com.github.stephenenright.walletwatchlist.web.api.wallet.repository.WalletRepository
import com.github.stephenenright.walletwatchlist.web.api.wallet.repository.WatchedWalletRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType

import static org.hamcrest.Matchers.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class WatchedWalletAPIControllerIntegrationTest extends BaseApiControllerTest {

	@Autowired
	WatchedWalletRepository watchedWalletRepository

	@Autowired
	WalletRepository walletRepository

	@Autowired
	UserRepository userRepository

	@Autowired
	BlockChainRepository blockChainRepository

	private List<UUID> createdWatchedWalletIds = []
	private List<UUID> createdWalletIds = []
	private List<UUID> createdUserIds = []
	private List<UUID> createdBlockChainIds = []

	def cleanup() {
		if (createdWatchedWalletIds) {
			watchedWalletRepository.deleteAllById(createdWatchedWalletIds)
			createdWatchedWalletIds.clear()
		}
		if (createdWalletIds) {
			walletRepository.deleteAllById(createdWalletIds)
			createdWalletIds.clear()
		}
		if (createdUserIds) {
			userRepository.deleteAllById(createdUserIds)
			createdUserIds.clear()
		}
		if (createdBlockChainIds) {
			blockChainRepository.deleteAllById(createdBlockChainIds)
			createdBlockChainIds.clear()
		}
	}

	private BlockChain getOrCreateTestBlockChain() {
		def existing = blockChainRepository.findByCode("WW_API_TEST")
		if (existing.isPresent()) {
			return existing.get()
		}
		def saved = blockChainRepository.save(BlockChainFixtureHelper.createBlockChain("WW_API_TEST", "Watched Wallet API Test", "mainnet", "TEST"))
		createdBlockChainIds << saved.id
		return saved
	}

	private User saveUser(User user) {
		def saved = userRepository.save(user)
		createdUserIds << saved.id
		return saved
	}

	private Wallet saveWallet(Wallet wallet) {
		def saved = walletRepository.save(wallet)
		createdWalletIds << saved.id
		return saved
	}

	def "list watched wallets returns paged results"() {
		given:
		def blockChain = getOrCreateTestBlockChain()
		def user = saveUser(UserFixtureHelper.createUser("Test", "List", "test.list.ww@example.com"))
		def wallet = saveWallet(WalletFixtureHelper.createWallet("0xwwlist111111111111111111111111111111111", blockChain))
		def watchedWallet = watchedWalletRepository.save(WatchedWalletFixtureHelper.createWatchedWallet(user, wallet, "My Wallet"))
		createdWatchedWalletIds << watchedWallet.id

		when:
		def result = mockMvc.perform(get("/api/watched-wallets")
				.contentType(MediaType.APPLICATION_JSON))

		then:
		result.andExpect(status().isOk())
				.andExpect(jsonPath('$.status', is("SUCCESS")))
				.andExpect(jsonPath('$.data.results', hasSize(greaterThanOrEqualTo(1))))
				.andExpect(jsonPath('$.data.pageNumber', is(1)))
				.andExpect(jsonPath('$.data.results[*].id', everyItem(notNullValue())))
				.andExpect(jsonPath('$.data.results[*].watcherId', everyItem(notNullValue())))
				.andExpect(jsonPath('$.data.results[*].walletId', everyItem(notNullValue())))
	}

	def "list watched wallets filters by watcherId"() {
		given:
		def blockChain = getOrCreateTestBlockChain()
		def user1 = saveUser(UserFixtureHelper.createUser("User", "One", "user.one.wwfilter@example.com"))
		def user2 = saveUser(UserFixtureHelper.createUser("User", "Two", "user.two.wwfilter@example.com"))
		def wallet1 = saveWallet(WalletFixtureHelper.createWallet("0xwwfilter1111111111111111111111111111111", blockChain))
		def wallet2 = saveWallet(WalletFixtureHelper.createWallet("0xwwfilter2222222222222222222222222222222", blockChain))
		def ww1 = watchedWalletRepository.save(WatchedWalletFixtureHelper.createWatchedWallet(user1, wallet1))
		def ww2 = watchedWalletRepository.save(WatchedWalletFixtureHelper.createWatchedWallet(user2, wallet2))
		createdWatchedWalletIds << ww1.id
		createdWatchedWalletIds << ww2.id

		when:
		def result = mockMvc.perform(get("/api/watched-wallets")
				.param("watcherId", user1.id.toString())
				.contentType(MediaType.APPLICATION_JSON))

		then:
		result.andExpect(status().isOk())
				.andExpect(jsonPath('$.status', is("SUCCESS")))
				.andExpect(jsonPath('$.data.results[*].watcherId', everyItem(is(user1.id.toString()))))
	}

	def "list watched wallets filters by status"() {
		given:
		def blockChain = getOrCreateTestBlockChain()
		def user = saveUser(UserFixtureHelper.createUser("Test", "StatusFilter", "test.statusfilter.ww@example.com"))
		def wallet1 = saveWallet(WalletFixtureHelper.createWallet("0xwwstatus111111111111111111111111111111", blockChain))
		def wallet2 = saveWallet(WalletFixtureHelper.createWallet("0xwwstatus222222222222222222222222222222", blockChain))
		def activeWW = watchedWalletRepository.save(WatchedWalletFixtureHelper.createWatchedWallet(user, wallet1, "Active", WatchedWalletStatus.ACTIVE))
		def pausedWW = watchedWalletRepository.save(WatchedWalletFixtureHelper.createWatchedWallet(user, wallet2, "Paused", WatchedWalletStatus.PAUSED))
		createdWatchedWalletIds << activeWW.id
		createdWatchedWalletIds << pausedWW.id

		when:
		def result = mockMvc.perform(get("/api/watched-wallets")
				.param("status", "PAUSED")
				.contentType(MediaType.APPLICATION_JSON))

		then:
		result.andExpect(status().isOk())
				.andExpect(jsonPath('$.status', is("SUCCESS")))
				.andExpect(jsonPath('$.data.results[*].status', everyItem(is("PAUSED"))))
	}

	def "get watched wallet by id returns watched wallet with all fields"() {
		given:
		def blockChain = getOrCreateTestBlockChain()
		def user = saveUser(UserFixtureHelper.createUser("Test", "GetById", "test.getbyid.ww@example.com"))
		def wallet = saveWallet(WalletFixtureHelper.createWallet("0xwwgetbyid33333333333333333333333333333", blockChain))
		def watchedWallet = watchedWalletRepository.save(WatchedWalletFixtureHelper.createWatchedWallet(user, wallet, "My Test Wallet"))
		createdWatchedWalletIds << watchedWallet.id

		when:
		def result = mockMvc.perform(get("/api/watched-wallets/{id}", watchedWallet.id))

		then:
		result.andExpect(status().isOk())
				.andExpect(jsonPath('$.status', is("SUCCESS")))
				.andExpect(jsonPath('$.data.id', is(watchedWallet.id.toString())))
				.andExpect(jsonPath('$.data.watcherId', is(user.id.toString())))
				.andExpect(jsonPath('$.data.watcherEmail', is(user.email)))
				.andExpect(jsonPath('$.data.walletId', is(wallet.id.toString())))
				.andExpect(jsonPath('$.data.walletAddress', is(wallet.address)))
				.andExpect(jsonPath('$.data.walletBlockChainCode', is("WW_API_TEST")))
				.andExpect(jsonPath('$.data.label', is("My Test Wallet")))
				.andExpect(jsonPath('$.data.status', is("ACTIVE")))
				.andExpect(jsonPath('$.data.dateCreated', notNullValue()))
	}

	def "get watched wallet by id returns not found for unknown id"() {
		when:
		def result = mockMvc.perform(get("/api/watched-wallets/{id}", UUID.randomUUID()))

		then:
		result.andExpect(status().isNotFound())
				.andExpect(jsonPath('$.status', is("NOT_FOUND")))
	}

	def "create watched wallet successfully"() {
		given:
		def blockChain = getOrCreateTestBlockChain()
		def user = saveUser(UserFixtureHelper.createUser("Test", "Create", "test.create.ww@example.com"))
		def wallet = saveWallet(WalletFixtureHelper.createWallet("0xwwcreate44444444444444444444444444444", blockChain))

		def requestBody = """
			{
				"watcherId": "${user.id}",
				"walletId": "${wallet.id}",
				"label": "New Watched Wallet"
			}
		"""

		when:
		def result = mockMvc.perform(post("/api/watched-wallets")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))

		then:
		result.andExpect(status().isCreated())
				.andExpect(jsonPath('$.status', is("SUCCESS")))
				.andExpect(jsonPath('$.data.id', notNullValue()))
				.andExpect(jsonPath('$.data.watcherId', is(user.id.toString())))
				.andExpect(jsonPath('$.data.walletId', is(wallet.id.toString())))
				.andExpect(jsonPath('$.data.label', is("New Watched Wallet")))
				.andExpect(jsonPath('$.data.status', is("ACTIVE")))
				.andExpect(jsonPath('$.data.dateCreated', notNullValue()))

		cleanup:
		def created = watchedWalletRepository.findByWatcherIdAndWalletId(user.id, wallet.id)
		if (created.isPresent()) {
			createdWatchedWalletIds << created.get().id
		}
	}

	def "create watched wallet fails when watcher does not exist"() {
		given:
		def blockChain = getOrCreateTestBlockChain()
		def wallet = saveWallet(WalletFixtureHelper.createWallet("0xwwnowatcher555555555555555555555555555", blockChain))

		def requestBody = """
			{
				"watcherId": "${UUID.randomUUID()}",
				"walletId": "${wallet.id}",
				"label": "Test"
			}
		"""

		when:
		def result = mockMvc.perform(post("/api/watched-wallets")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))

		then:
		result.andExpect(status().isBadRequest())
				.andExpect(jsonPath('$.error.validationErrors.watcherId', notNullValue()))
	}

	def "create watched wallet fails when wallet does not exist"() {
		given:
		def user = saveUser(UserFixtureHelper.createUser("Test", "NoWallet", "test.nowallet.ww@example.com"))

		def requestBody = """
			{
				"watcherId": "${user.id}",
				"walletId": "${UUID.randomUUID()}",
				"label": "Test"
			}
		"""

		when:
		def result = mockMvc.perform(post("/api/watched-wallets")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))

		then:
		result.andExpect(status().isBadRequest())
				.andExpect(jsonPath('$.error.validationErrors.walletId', notNullValue()))
	}

	def "create watched wallet fails when already watching"() {
		given:
		def blockChain = getOrCreateTestBlockChain()
		def user = saveUser(UserFixtureHelper.createUser("Test", "Duplicate", "test.duplicate.ww@example.com"))
		def wallet = saveWallet(WalletFixtureHelper.createWallet("0xwwduplicate666666666666666666666666666", blockChain))
		def existing = watchedWalletRepository.save(WatchedWalletFixtureHelper.createWatchedWallet(user, wallet, "Existing"))
		createdWatchedWalletIds << existing.id

		def requestBody = """
			{
				"watcherId": "${user.id}",
				"walletId": "${wallet.id}",
				"label": "Duplicate"
			}
		"""

		when:
		def result = mockMvc.perform(post("/api/watched-wallets")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))

		then:
		result.andExpect(status().isBadRequest())
				.andExpect(jsonPath('$.error.validationErrors.walletId', notNullValue()))
	}

	def "update watched wallet updates label and status successfully"() {
		given:
		def blockChain = getOrCreateTestBlockChain()
		def user = saveUser(UserFixtureHelper.createUser("Test", "Update", "test.update.ww@example.com"))
		def wallet = saveWallet(WalletFixtureHelper.createWallet("0xwwupdate7777777777777777777777777777777", blockChain))
		def watchedWallet = watchedWalletRepository.save(WatchedWalletFixtureHelper.createWatchedWallet(user, wallet, "Original"))
		createdWatchedWalletIds << watchedWallet.id

		def requestBody = """
			{
				"label": "Updated Label",
				"status": "PAUSED"
			}
		"""

		when:
		def result = mockMvc.perform(put("/api/watched-wallets/{id}", watchedWallet.id)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))

		then:
		result.andExpect(status().isOk())
				.andExpect(jsonPath('$.status', is("SUCCESS")))
				.andExpect(jsonPath('$.data.label', is("Updated Label")))
				.andExpect(jsonPath('$.data.status', is("PAUSED")))
	}

	def "update watched wallet returns not found for unknown id"() {
		given:
		def requestBody = """
			{
				"label": "Test"
			}
		"""

		when:
		def result = mockMvc.perform(put("/api/watched-wallets/{id}", UUID.randomUUID())
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))

		then:
		result.andExpect(status().isNotFound())
				.andExpect(jsonPath('$.status', is("NOT_FOUND")))
	}

	def "delete watched wallet removes entry successfully"() {
		given:
		def blockChain = getOrCreateTestBlockChain()
		def user = saveUser(UserFixtureHelper.createUser("Test", "Delete", "test.delete.ww@example.com"))
		def wallet = saveWallet(WalletFixtureHelper.createWallet("0xwwdelete8888888888888888888888888888888", blockChain))
		def watchedWallet = watchedWalletRepository.save(WatchedWalletFixtureHelper.createWatchedWallet(user, wallet))
		def watchedWalletId = watchedWallet.id

		when:
		def result = mockMvc.perform(delete("/api/watched-wallets/{id}", watchedWalletId))

		then:
		result.andExpect(status().isOk())
				.andExpect(jsonPath('$.status', is("SUCCESS")))

		and:
		watchedWalletRepository.findById(watchedWalletId).isEmpty()
		walletRepository.findById(wallet.id).isPresent()
	}

	def "delete watched wallet returns not found for unknown id"() {
		when:
		def result = mockMvc.perform(delete("/api/watched-wallets/{id}", UUID.randomUUID()))

		then:
		result.andExpect(status().isNotFound())
				.andExpect(jsonPath('$.status', is("NOT_FOUND")))
	}
}
