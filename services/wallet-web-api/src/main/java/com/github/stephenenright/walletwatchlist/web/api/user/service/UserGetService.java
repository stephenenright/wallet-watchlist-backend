/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.user.service;

import com.github.stephenenright.walletwatchlist.web.api.user.domain.User;
import com.github.stephenenright.walletwatchlist.web.api.user.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;

import com.github.stephenenright.walletwatchlist.web.api.user.validation.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserGetService {

	private final UserRepository userRepository;

	@Transactional(readOnly = true)
	public Page<User> findAll(Pageable pageable) {
		return userRepository.findAllByOrderByLastNameAscFirstNameAsc(pageable);
	}

	@Transactional(readOnly = true)
	public Optional<User> findById(UUID id) {
		return userRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmailIgnoreCase(email);
	}
}
