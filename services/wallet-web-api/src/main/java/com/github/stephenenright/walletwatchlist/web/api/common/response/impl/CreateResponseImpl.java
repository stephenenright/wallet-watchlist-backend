/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.common.response.impl;

import com.github.stephenenright.walletwatchlist.web.api.common.response.CreateResponse;
import com.github.stephenenright.walletwatchlist.web.api.common.response.ErrorType;
import com.github.stephenenright.walletwatchlist.web.api.common.validation.ValidationError;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class CreateResponseImpl<T> implements CreateResponse<T> {

	private final T result;
	private final boolean created;
	private final String message;
	private final Map<String, ValidationError> validationErrors;
	private final String errorMessage;
	private final String errorCode;
	private final ErrorType errorType;
	private final boolean notFound;

	@Override
	public boolean hasValidationErrors() {
		return validationErrors != null && !validationErrors.isEmpty();
	}

	@Override
	public Optional<Map<String, ValidationError>> getValidationErrors() {
		return Optional.ofNullable(validationErrors);
	}

	@Override
	public Optional<String> getErrorMessage() {
		return Optional.ofNullable(errorMessage);
	}

	@Override
	public Optional<String> getErrorCode() {
		return Optional.ofNullable(errorCode);
	}

	@Override
	public Optional<T> getResult() {
		return Optional.ofNullable(result);
	}

	@Override
	public Optional<String> getMessage() {
		return Optional.ofNullable(message);
	}

	@Override
	public boolean isCreated() {
		return created;
	}

	@Override
	public Optional<ErrorType> getErrorType() {
		return Optional.ofNullable(errorType);
	}

	@Override
	public boolean notFound() {
		return notFound;
	}
}
