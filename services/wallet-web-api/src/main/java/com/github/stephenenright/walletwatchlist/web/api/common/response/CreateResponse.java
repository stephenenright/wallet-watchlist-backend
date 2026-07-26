/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.common.response;

import com.github.stephenenright.walletwatchlist.web.api.common.response.impl.CreateResponseImpl;
import com.github.stephenenright.walletwatchlist.web.api.common.validation.ValidationError;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public interface CreateResponse<T> {

	Optional<T> getResult();

	boolean notFound();

	boolean hasValidationErrors();

	Optional<Map<String, ValidationError>> getValidationErrors();

	Optional<String> getErrorMessage();

	Optional<String> getErrorCode();

	Optional<ErrorType> getErrorType();

	Optional<String> getMessage();

	boolean isCreated();

	default void ifCreatedWithResult(Consumer<? super T> consumer) {
		if (isCreated() && getResult().isPresent()) {
			consumer.accept(getResult().get());
		}
	}

	static <T> CreateResponse<T> createSuccessResponse(T result) {
		return CreateResponseImpl.<T>builder().created(true).result(result).build();
	}

	static <T> CreateResponse<T> createSuccessResponse(T result, String message) {
		return CreateResponseImpl.<T>builder().created(true).result(result).message(message).build();
	}

	static <T> CreateResponse<T> createValidationFailedResponse(Map<String, ValidationError> errors) {
		return CreateResponseImpl.<T>builder().created(false).validationErrors(errors).errorType(ErrorType.BAD_REQUEST)
				.errorMessage("Validation failed").build();
	}

	static <T> CreateResponse<T> createFailedResponse(String errorMessage) {
		return createFailedResponse(errorMessage, ErrorType.INTERNAL_SERVER_ERROR);
	}

	static <T> CreateResponse<T> createFailedResponse(String errorMessage, ErrorType errorType) {
		return CreateResponseImpl.<T>builder().created(false).errorType(errorType).errorMessage(errorMessage).build();
	}

	static <T> CreateResponse<T> createNotFoundResponse() {
		return CreateResponseImpl.<T>builder().errorMessage("Item not found").notFound(true)
				.errorType(ErrorType.NOT_FOUND).build();
	}

	static <T> CreateResponse<T> unauthorizedResponse() {
		return CreateResponseImpl.<T>builder().errorMessage("Forbidden").errorType(ErrorType.UNAUTHORIZED).build();
	}
}
