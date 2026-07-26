/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.common.validation;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BaseValidator {

	private final SpringValidator springValidator;

	public ValidationErrorBuilder validate(Object request) {
		return validate(request, new ValidationErrorBuilder());
	}

	protected ValidationErrorBuilder validate(Object request, ValidationErrorBuilder builder) {
		springValidator.validate(request, builder);
		return builder;
	}
}
