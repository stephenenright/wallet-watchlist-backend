/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.common.models.entity;

import com.github.stephenenright.walletwatchlist.web.api.common.models.entity.generator.IdUuidTimeBasedGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public abstract class BaseEntity {

	@Id
	@IdUuidTimeBasedGenerator
	private UUID id;

	@Column(nullable = false, updatable = false)
	private Instant dateCreated;

	@Column
	private Instant dateUpdated;

	@PrePersist
	protected void onCreate() {
		dateCreated = Instant.now();
		dateUpdated = Instant.now();
	}

	@PreUpdate
	protected void onUpdate() {
		dateUpdated = Instant.now();
	}
}
