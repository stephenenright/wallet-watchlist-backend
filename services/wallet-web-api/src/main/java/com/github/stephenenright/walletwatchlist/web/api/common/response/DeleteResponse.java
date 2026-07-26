/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.common.response;

import com.github.stephenenright.walletwatchlist.web.api.common.response.impl.DeleteResponseImpl;
import com.github.stephenenright.walletwatchlist.web.api.common.validation.ValidationError;
import java.util.Map;
import java.util.Optional;

public interface DeleteResponse<T> {

	boolean isNotFound();

	boolean isDeleted();

	Optional<String> getMessage();

	Optional<String> getErrorMessage();

	Optional<String> getErrorCode();

	Optional<ErrorType> getErrorType();

	Optional<T> getResult();

	Optional<Map<String, ValidationError>> getValidationErrors();

	static <T> DeleteResponse<T> createSuccessResponse() {
		return DeleteResponseImpl.<T>builder().notFound(false).deleted(true).build();
	}

	static <T> DeleteResponse<T> createSuccessResponse(String message) {
		return DeleteResponseImpl.<T>builder().notFound(false).deleted(true).message(message).build();
	}

	static <T> DeleteResponse<T> createSuccessResponse(T result) {
		return DeleteResponseImpl.<T>builder().notFound(false).deleted(true).result(result).build();
	}

	static <T> DeleteResponse<T> createFailureResponse() {
		return DeleteResponseImpl.<T>builder().notFound(false).deleted(false).build();
	}

	static <T> DeleteResponse<T> createFailureResponse(String errorMessage) {
		return DeleteResponseImpl.<T>builder().notFound(false).deleted(false).errorMessage(errorMessage).build();
	}

	static <T> DeleteResponse<T> createFailureResponse(String errorMessage, ErrorType errorType) {
		return DeleteResponseImpl.<T>builder().notFound(false).deleted(false).errorMessage(errorMessage)
				.errorType(errorType).build();
	}

	static <T> DeleteResponse<T> createNotFoundResponse() {
		return DeleteResponseImpl.<T>builder().notFound(true).deleted(false).errorMessage("Item not found")
				.errorType(ErrorType.NOT_FOUND).build();
	}

	static <T> DeleteResponse<T> unauthorizedResponse() {
		return DeleteResponseImpl.<T>builder().errorMessage("Forbidden").errorType(ErrorType.UNAUTHORIZED).build();
	}
}
