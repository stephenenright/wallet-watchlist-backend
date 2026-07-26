/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.user.validation;

import com.github.stephenenright.walletwatchlist.web.api.common.validation.BaseValidator;
import com.github.stephenenright.walletwatchlist.web.api.common.validation.SpringValidator;
import com.github.stephenenright.walletwatchlist.web.api.common.validation.ValidationErrorBuilder;
import com.github.stephenenright.walletwatchlist.web.api.common.validation.ValidationResult;
import com.github.stephenenright.walletwatchlist.web.api.user.dto.request.CreateUserRequestDTO;
import com.github.stephenenright.walletwatchlist.web.api.user.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserValidator extends BaseValidator {

	private final UserRepository userRepository;

	public UserValidator(SpringValidator validator, UserRepository userRepository) {
		super(validator);
		this.userRepository = userRepository;
	}

	public ValidationResult validateForCreate(CreateUserRequestDTO request) {
		ValidationErrorBuilder builder = validate(request);

		if (builder.isValid() && request.getEmail() != null
				&& userRepository.existsByEmailIgnoreCase(request.getEmail())) {
			builder.addExistsError("email");
		}

		if (builder.hasErrors()) {
			return ValidationResult.invalid(builder.getErrors());
		}

		return ValidationResult.valid();
	}
}
