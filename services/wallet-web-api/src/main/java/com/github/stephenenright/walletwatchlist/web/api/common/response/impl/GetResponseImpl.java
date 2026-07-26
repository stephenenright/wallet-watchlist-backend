/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.common.response.impl;

import com.github.stephenenright.walletwatchlist.web.api.common.response.ErrorType;
import com.github.stephenenright.walletwatchlist.web.api.common.response.GetResponse;
import com.github.stephenenright.walletwatchlist.web.api.common.validation.ValidationError;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class GetResponseImpl<RT> implements GetResponse<RT> {

	private final Map<String, ValidationError> validationErrors;
	private final String errorMessage;
	private final String errorCode;
	private final RT result;
	private final boolean notFoundError;
	private final boolean notAuthorized;
	private final ErrorType errorType;

	@Override
	public boolean isOk() {
		return !isValidationErrors() && errorMessage == null && !notFoundError && !notAuthorized;
	}

	@Override
	public boolean isNotFoundError() {
		return notFoundError;
	}

	@Override
	public boolean isNotAuthorized() {
		return notAuthorized;
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
	public Optional<RT> getResult() {
		return Optional.ofNullable(result);
	}

	@Override
	public Optional<ErrorType> getErrorType() {
		return Optional.ofNullable(errorType);
	}

	private boolean isValidationErrors() {
		return validationErrors != null && !validationErrors.isEmpty();
	}
}
