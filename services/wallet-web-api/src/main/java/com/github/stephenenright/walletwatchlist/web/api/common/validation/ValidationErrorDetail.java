package com.github.stephenenright.walletwatchlist.web.api.common.validation;

public record ValidationErrorDetail(ValidationErrorType errorType, String error) {

	public static ValidationErrorDetail ofError(ValidationErrorType errorType, String error) {
		return new ValidationErrorDetail(errorType, error);
	}
}
