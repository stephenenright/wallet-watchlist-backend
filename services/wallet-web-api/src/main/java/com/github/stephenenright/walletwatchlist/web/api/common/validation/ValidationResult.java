package com.github.stephenenright.walletwatchlist.web.api.common.validation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
public class ValidationResult {
	private boolean valid;
	private Map<String, ValidationError> errors;

	public boolean isNotValid() {
		return !valid;
	}

	public void addErrors(Map<String, ValidationError> errors) {
		this.errors.putAll(errors);
		updateIsValid();
	}

	private void updateIsValid() {
		this.valid = errors.isEmpty();
	}

	public static ValidationResult valid() {
		return ValidationResult.builder().valid(true).build();
	}

	public static ValidationResult invalid(Map<String, ValidationError> errors) {
		return ValidationResult.builder().valid(false).errors(errors).build();
	}
}
