/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.asset.domain;

import com.github.stephenenright.walletwatchlist.web.api.common.models.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "currency")
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Currency extends BaseEntity {

	@Column(nullable = false, unique = true)
	private String symbol;

	@Column(nullable = false)
	private String name;
}
