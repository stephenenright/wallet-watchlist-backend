package com.github.stephenenright.walletwatchlist.web.api.common.validation;

import java.util.List;

public class ValidationErrorStandard extends ValidationError {

	private List<ValidationErrorDetail> errors;

	public ValidationErrorStandard(String label, ValidationErrorDetail detail) {
		this(label, List.of(detail));
	}

	public ValidationErrorStandard(String label, List<ValidationErrorDetail> errors) {
		super(label);
		this.errors = errors;
	}

	@Override
	public List<ValidationErrorDetail> getErrors() {
		return errors;
	}
}
