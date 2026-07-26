/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.common.validation;

import jakarta.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Component
public class SpringValidator {

	private final LocalValidatorFactoryBean validator;

	public SpringValidator(LocalValidatorFactoryBean validator) {
		this.validator = validator;
	}

	public <T> ValidationErrorBuilder validate(T dto) {
		return validate(dto, new ValidationErrorBuilder());
	}

	public <T> ValidationErrorBuilder validate(T dto, ValidationErrorBuilder builder) {
		Set<ConstraintViolation<T>> violations = validator.validate(dto);

		Map<String, ValidationError> errors = buildValidationErrors(violations);
		builder.addAllErrors(errors);

		return builder;
	}

	public <T> Map<String, ValidationError> validateToMap(T dto) {
		Set<ConstraintViolation<T>> violations = validator.validate(dto);
		return buildValidationErrors(violations);
	}

	private <T> Map<String, ValidationError> buildValidationErrors(Set<ConstraintViolation<T>> violations) {
		Map<String, ValidationError> errors = new HashMap<>();

		for (ConstraintViolation<T> violation : violations) {
			String field = violation.getPropertyPath().toString();
			String message = violation.getMessage();
			String constraintName = violation.getConstraintDescriptor().getAnnotation().annotationType()
					.getSimpleName();

			ValidationErrorType errorType = mapConstraintToErrorType(constraintName);

			ValidationError existingError = errors.get(field);
			if (existingError instanceof ValidationErrorStandard standard) {
				List<ValidationErrorDetail> existingDetails = new ArrayList<>(standard.getErrors());
				existingDetails.add(ValidationErrorDetail.ofError(errorType, message));
				errors.put(field, new ValidationErrorStandard(field, existingDetails));
			} else {
				errors.put(field,
						new ValidationErrorStandard(field, ValidationErrorDetail.ofError(errorType, message)));
			}
		}

		return errors;
	}

	private ValidationErrorType mapConstraintToErrorType(String constraintName) {
		return switch (constraintName) {
			case "NotNull", "NotBlank", "NotEmpty" -> ValidationErrorType.REQUIRED;
			case "Email" -> ValidationErrorType.INVALID_EMAIL;
			case "Size" -> ValidationErrorType.INVALID_LENGTH;
			case "Min" -> ValidationErrorType.INVALID_MIN_VALUE;
			case "Max" -> ValidationErrorType.INVALID_MAX_VALUE;
			case "Pattern" -> ValidationErrorType.INVALID_PATTERN;
			case "Past", "PastOrPresent", "Future", "FutureOrPresent" -> ValidationErrorType.INVALID_DATE;
			default -> ValidationErrorType.INVALID_VALUE;
		};
	}
}
