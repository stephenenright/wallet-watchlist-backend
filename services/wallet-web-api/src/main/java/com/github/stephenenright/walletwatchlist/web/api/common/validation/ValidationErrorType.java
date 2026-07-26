package com.github.stephenenright.walletwatchlist.web.api.common.validation;

import com.github.stephenenright.walletwatchlist.web.api.common.util.ErrorMessages;
import lombok.Getter;

@Getter
public enum ValidationErrorType {
	REQUIRED("required"), NOT_FOUND("notFound"), DOES_NOT_EXIST("doesNotExist"), EXISTS("exists"), INVALID_DATE(
			"dateInvalid"), INVALID_FORMAT("invalidFormat"), INVALID_VALUE("invalidValue"), INVALID_LENGTH(
					"invalidLength"), INVALID_MIN_LENGTH(
							"invalidMinLength"), INVALID_MAX_LENGTH("invalidMaxLength"), INVALID_MIN_VALUE(
									"invalidMinLength"), INVALID_MAX_VALUE("invalidMaxLength"), INVALID_PATTERN(
											"invalidPattern"), INVALID_EMAIL("invalidEmail");

	private final String code;

	ValidationErrorType(String code) {
		this.code = code;
	}

	public static ValidationErrorDetail required() {
		return required(ErrorMessages.errorRequired());
	}

	public static ValidationErrorDetail required(String error) {
		return error(REQUIRED, error);
	}

	public static ValidationErrorDetail notFound() {
		return required(ErrorMessages.itemNotFound());
	}

	public static ValidationErrorDetail notFound(String error) {
		return error(NOT_FOUND, error);
	}

	public static ValidationErrorDetail exists() {
		return exists(ErrorMessages.errorGenericValueExists());
	}

	public static ValidationErrorDetail exists(String error) {
		return error(EXISTS, error);
	}

	public static ValidationErrorDetail doesNotExistForLabel(String label) {
		return exists(ErrorMessages.errorDoesNotExist(label));
	}

	public static ValidationErrorDetail doesNotExist(String error) {
		return error(DOES_NOT_EXIST, error);
	}

	public static ValidationErrorDetail minLength(int minLength) {
		return exists(ErrorMessages.errorMinLength(minLength));
	}

	public static ValidationErrorDetail minLength(String error) {
		return error(INVALID_MIN_LENGTH, error);
	}

	public static ValidationErrorDetail dateInvalid() {
		return exists(ErrorMessages.dateInvalid());
	}

	public static ValidationErrorDetail dateInvalid(String error) {
		return error(INVALID_DATE, error);
	}

	public static ValidationErrorDetail maxLength(int maxLength) {
		return exists(ErrorMessages.errorMinLength(maxLength));
	}

	public static ValidationErrorDetail maxLength(String error) {
		return error(INVALID_MAX_LENGTH, error);
	}

	public static ValidationErrorDetail error(ValidationErrorType errorType, String error) {
		return ValidationErrorDetail.ofError(errorType, error);
	}
}
