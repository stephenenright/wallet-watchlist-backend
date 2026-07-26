/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.user.service;

import com.github.stephenenright.walletwatchlist.web.api.common.response.CreateResponse;
import com.github.stephenenright.walletwatchlist.web.api.user.domain.User;
import com.github.stephenenright.walletwatchlist.web.api.user.dto.request.CreateUserRequestDTO;
import com.github.stephenenright.walletwatchlist.web.api.user.mapper.UserMapper;
import com.github.stephenenright.walletwatchlist.web.api.user.repository.UserRepository;
import com.github.stephenenright.walletwatchlist.web.api.user.validation.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserUpdateService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final UserValidator userValidator;

	@Transactional
	public CreateResponse<User> create(CreateUserRequestDTO request) {
		final var validationResult = userValidator.validateForCreate(request);

		if (validationResult.isNotValid()) {
			return CreateResponse.createValidationFailedResponse(validationResult.getErrors());
		}

		User user = userMapper.toEntity(request);
		return CreateResponse.createSuccessResponse(userRepository.save(user));
	}
}
