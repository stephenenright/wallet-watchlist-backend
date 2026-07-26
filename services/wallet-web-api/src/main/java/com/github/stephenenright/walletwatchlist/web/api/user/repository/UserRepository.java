/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.user.repository;

import com.github.stephenenright.walletwatchlist.web.api.common.repository.BaseJpaRepository;
import com.github.stephenenright.walletwatchlist.web.api.user.domain.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseJpaRepository<User, UUID> {

	Page<User> findAllByOrderByLastNameAscFirstNameAsc(Pageable pageable);

	@Query("SELECT u FROM User u WHERE LOWER(u.email) = LOWER(:email)")
	Optional<User> findByEmailIgnoreCase(String email);

	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE LOWER(u.email) = LOWER(:email)")
	boolean existsByEmailIgnoreCase(String email);
}
