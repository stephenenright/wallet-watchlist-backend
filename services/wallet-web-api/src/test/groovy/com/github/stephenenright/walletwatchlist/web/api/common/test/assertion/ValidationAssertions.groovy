/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.common.test.assertion

import com.github.stephenenright.walletwatchlist.web.api.common.validation.ValidationErrorType
import com.github.stephenenright.walletwatchlist.web.api.common.validation.ValidationResult

class ValidationAssertions {

	static void assertHasError(ValidationResult result, String field, ValidationErrorType errorType) {
		assert !result.valid : "Expected validation to fail but it was valid"
		assert result.errors != null : "Expected errors but errors was null"
		assert result.errors.containsKey(field) : "Expected error for field '${field}' but found: ${result.errors.keySet()}"

		def fieldError = result.errors.get(field)
		def hasErrorType = fieldError.errors.any { it.errorType == errorType }
		assert hasErrorType : "Expected error type ${errorType} for field '${field}' but found: ${fieldError.errors.collect { it.errorType }}"
	}

	static void assertHasRequiredError(ValidationResult result, String field) {
		assertHasError(result, field, ValidationErrorType.REQUIRED)
	}

	static void assertHasExistsError(ValidationResult result, String field) {
		assertHasError(result, field, ValidationErrorType.EXISTS)
	}

	static void assertHasInvalidEmailError(ValidationResult result, String field) {
		assertHasError(result, field, ValidationErrorType.INVALID_EMAIL)
	}

	static void assertHasNotFoundError(ValidationResult result, String field) {
		assertHasError(result, field, ValidationErrorType.NOT_FOUND)
	}

	static void assertIsValid(ValidationResult result) {
		assert result.valid : "Expected validation to pass but it failed with errors: ${result.errors?.keySet()}"
	}

	static void assertIsInvalid(ValidationResult result) {
		assert !result.valid : "Expected validation to fail but it was valid"
	}
}
