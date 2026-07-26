package com.github.stephenenright.walletwatchlist.web.api.common.validation;

import com.github.stephenenright.walletwatchlist.web.api.common.util.ErrorMessages;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import static com.github.stephenenright.walletwatchlist.web.api.common.validation.ValidationErrorDetail.ofError;

public class ValidationErrorBuilder {

	private Map<String, ValidationError> validationErrors = new HashMap<>();

	@Getter
	private Map<String, Object> resolvedValues = new HashMap<>();

	public ValidationErrorBuilder() {
		this(new HashMap<>());
	}

	public ValidationErrorBuilder(Map<String, ValidationError> validationErrors) {
		this.validationErrors = validationErrors;
	}

	public ValidationErrorBuilder addRequired(String key) {
		validationErrors.put(key,
				new ValidationErrorStandard(key, ofError(ValidationErrorType.REQUIRED, ErrorMessages.errorRequired())));
		return this;
	}

	public ValidationErrorBuilder addExistsError(String key) {
		validationErrors.put(key, new ValidationErrorStandard(key,
				ofError(ValidationErrorType.EXISTS, ErrorMessages.errorGenericValueExists())));
		return this;
	}

	public ValidationErrorBuilder addNotFoundError(String key) {
		validationErrors.put(key,
				new ValidationErrorStandard(key, ofError(ValidationErrorType.NOT_FOUND, ErrorMessages.itemNotFound())));
		return this;
	}

	public ValidationErrorBuilder addError(String key, ValidationErrorType errorType, String message) {
		validationErrors.put(key, new ValidationErrorStandard(key, ofError(errorType, message)));
		return this;
	}

	public ValidationErrorBuilder addError(String key, ValidationErrorDetail error) {
		validationErrors.put(key, new ValidationErrorStandard(key, error));
		return this;
	}

	public Map<String, ValidationError> getErrors() {
		return validationErrors;
	}

	public boolean hasErrors() {
		return validationErrors != null && !validationErrors.isEmpty();
	}

	public boolean isValid() {
		return !hasErrors();
	}

	public ValidationErrorBuilder addResolvedValue(String key, Object value) {
		resolvedValues.put(key, value);
		return this;
	}

	public ValidationErrorBuilder addAllErrors(Map<String, ValidationError> validationErrors) {
		this.validationErrors.putAll(validationErrors);
		return this;
	}

	public static ValidationErrorBuilder ofErrors(Map<String, ValidationError> errors) {
		ValidationErrorBuilder builder = new ValidationErrorBuilder();
		builder.validationErrors = errors;
		return builder;
	}
}
