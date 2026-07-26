/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.user.validation

import com.github.stephenenright.walletwatchlist.web.api.common.test.BaseAPIIntegrationTest
import com.github.stephenenright.walletwatchlist.web.api.user.domain.User
import com.github.stephenenright.walletwatchlist.web.api.user.dto.request.CreateUserRequestDTO
import com.github.stephenenright.walletwatchlist.web.api.user.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired

import java.util.UUID

import static com.github.stephenenright.walletwatchlist.web.api.common.test.assertion.ValidationAssertions.*

class UserValidatorIntegrationTest extends BaseAPIIntegrationTest {

	@Autowired
	UserValidator userValidator

	@Autowired
	UserRepository userRepository

	private List<UUID> createdUserIds = []

	def cleanup() {
		if (createdUserIds) {
			userRepository.deleteAllById(createdUserIds)
			createdUserIds.clear()
		}
	}

	private User saveUser(User user) {
		def saved = userRepository.save(user)
		createdUserIds << saved.id
		return saved
	}

	def "validateForCreate returns valid when all fields are valid"() {
		given:
		def request = CreateUserRequestDTO.builder()
				.firstName("Test")
				.lastName("User")
				.email("test.user.unique@example.com")
				.build()

		when:
		def result = userValidator.validateForCreate(request)

		then:
		assertIsValid(result)
	}

	def "validateForCreate returns invalid when email already exists"() {
		given:
		def existingUser = User.builder()
				.firstName("Jane")
				.lastName("Smith")
				.email("existing@example.com")
				.build()
		saveUser(existingUser)

		and:
		def request = CreateUserRequestDTO.builder()
				.firstName("John")
				.lastName("Doe")
				.email("existing@example.com")
				.build()

		when:
		def result = userValidator.validateForCreate(request)

		then:
		assertHasExistsError(result, "email")
	}

	def "validateForCreate returns invalid when email exists with different case"() {
		given:
		def existingUser = User.builder()
				.firstName("Jane")
				.lastName("Smith")
				.email("Test@Example.com")
				.build()
		saveUser(existingUser)

		and:
		def request = CreateUserRequestDTO.builder()
				.firstName("John")
				.lastName("Doe")
				.email("test@example.com")
				.build()

		when:
		def result = userValidator.validateForCreate(request)

		then:
		assertHasExistsError(result, "email")
	}

	def "validateForCreate returns invalid when email exists with uppercase"() {
		given:
		def existingUser = User.builder()
				.firstName("Jane")
				.lastName("Smith")
				.email("lowercase@example.com")
				.build()
		saveUser(existingUser)

		and:
		def request = CreateUserRequestDTO.builder()
				.firstName("John")
				.lastName("Doe")
				.email("LOWERCASE@EXAMPLE.COM")
				.build()

		when:
		def result = userValidator.validateForCreate(request)

		then:
		assertHasExistsError(result, "email")
	}

	def "validateForCreate returns invalid when firstName is blank"() {
		given:
		def request = CreateUserRequestDTO.builder()
				.firstName("")
				.lastName("Doe")
				.email("john.doe@example.com")
				.build()

		when:
		def result = userValidator.validateForCreate(request)

		then:
		assertHasRequiredError(result, "firstName")
	}

	def "validateForCreate returns invalid when lastName is blank"() {
		given:
		def request = CreateUserRequestDTO.builder()
				.firstName("John")
				.lastName("")
				.email("john.doe@example.com")
				.build()

		when:
		def result = userValidator.validateForCreate(request)

		then:
		assertHasRequiredError(result, "lastName")
	}

	def "validateForCreate returns invalid when email is blank"() {
		given:
		def request = CreateUserRequestDTO.builder()
				.firstName("John")
				.lastName("Doe")
				.email("")
				.build()

		when:
		def result = userValidator.validateForCreate(request)

		then:
		assertHasRequiredError(result, "email")
	}

	def "validateForCreate returns invalid when all required fields are blank"() {
		given:
		def request = CreateUserRequestDTO.builder()
				.firstName("")
				.lastName("")
				.email("")
				.build()

		when:
		def result = userValidator.validateForCreate(request)

		then:
		assertIsInvalid(result)
		assertHasRequiredError(result, "firstName")
		assertHasRequiredError(result, "lastName")
		assertHasRequiredError(result, "email")
	}

	def "validateForCreate returns invalid when email format is invalid"() {
		given:
		def request = CreateUserRequestDTO.builder()
				.firstName("John")
				.lastName("Doe")
				.email("invalid-email")
				.build()

		when:
		def result = userValidator.validateForCreate(request)

		then:
		assertHasInvalidEmailError(result, "email")
	}

	def "validateForCreate returns invalid when fields are null"() {
		given:
		def request = CreateUserRequestDTO.builder()
				.firstName(null)
				.lastName(null)
				.email(null)
				.build()

		when:
		def result = userValidator.validateForCreate(request)

		then:
		assertIsInvalid(result)
		assertHasRequiredError(result, "firstName")
		assertHasRequiredError(result, "lastName")
		assertHasRequiredError(result, "email")
	}
}
