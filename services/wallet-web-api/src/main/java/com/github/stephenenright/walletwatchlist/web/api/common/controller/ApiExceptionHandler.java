/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.common.controller;

import com.github.stephenenright.walletwatchlist.web.api.common.response.ApiResponse;
import com.github.stephenenright.walletwatchlist.web.api.common.response.ApiResponseUtils;
import com.github.stephenenright.walletwatchlist.web.api.common.validation.ValidationError;
import com.github.stephenenright.walletwatchlist.web.api.common.validation.ValidationErrorDetail;
import com.github.stephenenright.walletwatchlist.web.api.common.validation.ValidationErrorStandard;
import com.github.stephenenright.walletwatchlist.web.api.common.validation.ValidationErrorType;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiResponse<?> handleValidation(MethodArgumentNotValidException ex) {
		Map<String, ValidationError> validationErrors = new HashMap<>();
		for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
			String field = fieldError.getField();
			String message = fieldError.getDefaultMessage();
			validationErrors.put(field,
					new ValidationErrorStandard(field, ValidationErrorDetail.ofError(ValidationErrorType.INVALID_VALUE, message)));
		}
		return ApiResponseUtils.createValidationFailedErrorResponse("Validation failed", null, validationErrors);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiResponse<?> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
		String paramName = ex.getName();
		Object invalidValue = ex.getValue();
		Class<?> requiredType = ex.getRequiredType();
		String typeName = requiredType != null ? requiredType.getSimpleName() : "unknown";
		String message = String.format("Invalid value '%s'. Expected type: %s", invalidValue, typeName);

		Map<String, ValidationError> validationErrors = new HashMap<>();
		validationErrors.put(paramName,
				new ValidationErrorStandard(paramName, ValidationErrorDetail.ofError(ValidationErrorType.INVALID_VALUE, message)));

		return ApiResponseUtils.createValidationFailedErrorResponse("Validation failed", null, validationErrors);
	}

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<?> handleUnexpectedError(Throwable t) {
        log.error("An unexpected error occurred", t);
        return ApiResponseUtils.createErrorResponse("An unexpected error occurred");
    }
}
