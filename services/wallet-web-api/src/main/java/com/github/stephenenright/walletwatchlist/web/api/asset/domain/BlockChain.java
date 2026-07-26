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
@Table(name = "blockchain")
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class BlockChain extends BaseEntity {

	@Column(nullable = false, unique = true)
	private String code;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "mainnet", nullable = false)
	private String mainnet;

	@Column(name = "native_currency", nullable = false)
	private String nativeCurrency;
}
