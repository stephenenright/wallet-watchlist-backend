/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.common.response;

import com.github.stephenenright.walletwatchlist.web.api.common.response.impl.GetResponseImpl;
import com.github.stephenenright.walletwatchlist.web.api.common.validation.ValidationError;
import java.util.Map;
import java.util.Optional;

public interface GetResponse<RT> {

	boolean isOk();

	boolean isNotFoundError();

	boolean isNotAuthorized();

	Optional<Map<String, ValidationError>> getValidationErrors();

	Optional<String> getErrorMessage();

	Optional<String> getErrorCode();

	Optional<ErrorType> getErrorType();

	Optional<RT> getResult();

	static <RT> GetResponse<RT> createSuccessOrNotFound(RT result) {
		if (result == null) {
			return createNotFoundResponse();
		}
		return createSuccessResponse(result);
	}

	static <RT> GetResponse<RT> createSuccessResponse(RT result) {
		return GetResponseImpl.<RT>builder().result(result).build();
	}

	static <T> GetResponse<T> createNotFoundResponse() {
		return GetResponseImpl.<T>builder().notFoundError(true).errorType(ErrorType.NOT_FOUND)
				.errorMessage("Item not found").build();
	}

	static <T> GetResponse<T> createNotFoundResponse(String errorMessage) {
		return GetResponseImpl.<T>builder().notFoundError(true).errorMessage(errorMessage)
				.errorType(ErrorType.NOT_FOUND).build();
	}

	static <T> GetResponse<T> createNotAuthorizedResponse() {
		return GetResponseImpl.<T>builder().notAuthorized(true).errorType(ErrorType.UNAUTHORIZED).build();
	}

	static <T> GetResponse<T> createValidationFailedResponse(Map<String, ValidationError> validationErrors) {
		return GetResponseImpl.<T>builder().validationErrors(validationErrors).errorType(ErrorType.BAD_REQUEST)
				.errorMessage("Validation failed").build();
	}

	static <T> GetResponse<T> createErrorResponse(String errorMessage) {
		return GetResponseImpl.<T>builder().errorMessage(errorMessage).errorType(ErrorType.INTERNAL_SERVER_ERROR)
				.build();
	}

	static <T> GetResponse<T> createErrorResponse(String errorMessage, ErrorType errorType) {
		return GetResponseImpl.<T>builder().errorMessage(errorMessage).errorType(errorType).build();
	}
}
