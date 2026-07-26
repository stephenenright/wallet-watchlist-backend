/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.common.response;

import com.github.stephenenright.walletwatchlist.web.api.common.response.impl.UpdateResponseImpl;
import com.github.stephenenright.walletwatchlist.web.api.common.validation.ValidationError;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public interface UpdateResponse<T> {

	boolean isNotFound();

	boolean isUpdated();

	Optional<T> getResult();

	Optional<Map<String, ValidationError>> getValidationErrors();

	Optional<String> getErrorMessage();

	Optional<String> getErrorCode();

	Optional<ErrorType> getErrorType();

	Optional<String> getMessage();

	default void ifUpdatedWithResult(Consumer<? super T> consumer) {
		if (isUpdated() && getResult().isPresent()) {
			consumer.accept(getResult().get());
		}
	}

	static <T> UpdateResponse<T> createSuccessResponse(T result) {
		return UpdateResponseImpl.<T>builder().updated(true).result(result).build();
	}

	static <T> UpdateResponse<T> createSuccessResponse(T result, String message) {
		return UpdateResponseImpl.<T>builder().updated(true).result(result).message(message).build();
	}

	static <T> UpdateResponse<T> createValidationFailedResponse(Map<String, ValidationError> errors) {
		return UpdateResponseImpl.<T>builder().validationErrors(errors).errorType(ErrorType.BAD_REQUEST)
				.errorMessage("Validation failed").build();
	}

	static <T> UpdateResponse<T> createFailedResponse(String errorMessage) {
		return UpdateResponseImpl.<T>builder().errorMessage(errorMessage).build();
	}

	static <T> UpdateResponse<T> createFailedResponse(String errorMessage, ErrorType errorType) {
		return UpdateResponseImpl.<T>builder().errorMessage(errorMessage).errorType(errorType).build();
	}

	static <T> UpdateResponse<T> createNotFoundResponse() {
		return createNotFoundResponse("Item not found");
	}

	static <T> UpdateResponse<T> createNotFoundResponse(String errorMessage) {
		return UpdateResponseImpl.<T>builder().notFound(true).errorMessage(errorMessage).errorType(ErrorType.NOT_FOUND)
				.build();
	}

	static <T> UpdateResponse<T> unauthorizedResponse() {
		return UpdateResponseImpl.<T>builder().errorMessage("Forbidden").errorType(ErrorType.UNAUTHORIZED).build();
	}
}
