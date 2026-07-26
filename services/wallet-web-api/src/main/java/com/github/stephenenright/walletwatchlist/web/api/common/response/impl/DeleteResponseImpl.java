/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.common.response.impl;

import com.github.stephenenright.walletwatchlist.web.api.common.response.DeleteResponse;
import com.github.stephenenright.walletwatchlist.web.api.common.response.ErrorType;
import com.github.stephenenright.walletwatchlist.web.api.common.validation.ValidationError;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class DeleteResponseImpl<T> implements DeleteResponse<T> {

	private final T result;
	private final boolean deleted;
	private final boolean notFound;
	private final String message;
	private final Map<String, ValidationError> validationErrors;
	private final String errorMessage;
	private final String errorCode;
	private final ErrorType errorType;

	@Override
	public boolean isNotFound() {
		return notFound;
	}

	@Override
	public boolean isDeleted() {
		return deleted;
	}

	@Override
	public Optional<String> getMessage() {
		return Optional.ofNullable(message);
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
	public Optional<ErrorType> getErrorType() {
		return Optional.ofNullable(errorType);
	}

	@Override
	public Optional<T> getResult() {
		return Optional.ofNullable(result);
	}

	@Override
	public Optional<Map<String, ValidationError>> getValidationErrors() {
		return Optional.ofNullable(validationErrors);
	}
}
