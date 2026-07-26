/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.asset.domain;

import com.github.stephenenright.walletwatchlist.web.api.common.models.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "blockchain_asset", uniqueConstraints = @UniqueConstraint(columnNames = {"currency_id", "blockchain_id"}))
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class BlockchainAsset extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "currency_id", nullable = false)
	private Currency currency;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "blockchain_id", nullable = false)
	private BlockChain blockChain;

	@Column(name = "contract_address")
	private String contractAddress;

	@Column(name = "is_native", nullable = false)
	private boolean isNative;
}
