package com.github.stephenenright.walletwatchlist.web.api.common.jpa;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class JpaHelper {
	private final EntityManager entityManager;

	public <T> T save(T entity, Class<T> t) {
		entityManager.persist(entity);
		return entity;
	}
}
