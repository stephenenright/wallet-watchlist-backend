/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.user.controller

import com.github.stephenenright.walletwatchlist.web.api.common.test.BaseApiControllerTest
import com.github.stephenenright.walletwatchlist.web.api.user.fixture.integration.UserFixtureResult
import com.github.stephenenright.walletwatchlist.web.api.user.fixture.integration.UserFixtureSettings
import com.github.stephenenright.walletwatchlist.web.api.user.fixture.integration.UserIntegrationTestHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.transaction.annotation.Transactional

import static org.hamcrest.Matchers.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@Transactional
class UserApiControllerIntegrationTest extends BaseApiControllerTest {

	@Autowired
	UserIntegrationTestHelper userTestHelper

	UserFixtureResult fixtures

	def setup() {
		fixtures = userTestHelper.create(UserFixtureSettings.builder().build())
	}

	def "list users returns paged results ordered by last name, first name"() {
		when:
		def result = mockMvc.perform(get("/api/users")
				.contentType(MediaType.APPLICATION_JSON))

		then:
		result.andExpect(status().isOk())
				.andExpect(jsonPath('$.status', is("SUCCESS")))
				.andExpect(jsonPath('$.data.results', hasSize(greaterThanOrEqualTo(4))))
				.andExpect(jsonPath('$.data.pageNumber', is(1)))
				.andExpect(jsonPath('$.data.totalResults', greaterThanOrEqualTo(4)))
				.andExpect(jsonPath('$.data.results[0].lastName', is("Doe")))
				.andExpect(jsonPath('$.data.results[0].firstName', is("Jane")))
				.andExpect(jsonPath('$.data.results[1].lastName', is("Doe")))
				.andExpect(jsonPath('$.data.results[1].firstName', is("John")))
				.andExpect(jsonPath('$.data.results[*].id', everyItem(notNullValue())))
				.andExpect(jsonPath('$.data.results[*].email', everyItem(notNullValue())))
				.andExpect(jsonPath('$.data.results[*].dateCreated', everyItem(notNullValue())))
	}



	def "get user by id returns user with all fields"() {
		when:
		def result = mockMvc.perform(get("/api/users/{id}", fixtures.johnDoe().id))

		then:
		result.andExpect(status().isOk())
				.andExpect(jsonPath('$.status', is("SUCCESS")))
				.andExpect(jsonPath('$.data.id', is(fixtures.johnDoe().id.toString())))
				.andExpect(jsonPath('$.data.firstName', is("John")))
				.andExpect(jsonPath('$.data.lastName', is("Doe")))
				.andExpect(jsonPath('$.data.email', is("john.doe.test@example.com")))
				.andExpect(jsonPath('$.data.dateCreated', notNullValue()))
				.andExpect(jsonPath('$.data.dateUpdated', notNullValue()))
	}

	def "get user by id returns not found for unknown id"() {
		when:
		def result = mockMvc.perform(get("/api/users/{id}", UUID.randomUUID()))

		then:
		result.andExpect(status().isNotFound())
				.andExpect(jsonPath('$.status', is("NOT_FOUND")))
				.andExpect(jsonPath('$.error.message', notNullValue()))
	}

	def "create user successfully creates user with valid data"() {
		given:
		def requestBody = """
			{
				"firstName": "New",
				"lastName": "User",
				"email": "new.user@example.com"
			}
		"""

		when:
		def result = mockMvc.perform(post("/api/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))

		then:
		result.andExpect(status().isCreated())
				.andExpect(jsonPath('$.status', is("SUCCESS")))
				.andExpect(jsonPath('$.data.id', notNullValue()))
				.andExpect(jsonPath('$.data.firstName', is("New")))
				.andExpect(jsonPath('$.data.lastName', is("User")))
				.andExpect(jsonPath('$.data.email', is("new.user@example.com")))
				.andExpect(jsonPath('$.data.dateCreated', notNullValue()))
	}

	def "create user fails with blank first name"() {
		given:
		def requestBody = """
			{
				"firstName": "",
				"lastName": "User",
				"email": "test.blank.firstname@example.com"
			}
		"""

		when:
		def result = mockMvc.perform(post("/api/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))

		then:
		result.andExpect(status().isBadRequest())
				.andExpect(jsonPath('$.status', is(400)))
				.andExpect(jsonPath('$.error', is("Bad Request")))
				.andExpect(jsonPath('$.message', containsString("firstName")))
	}
}
