/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.wallet.domain;

import com.github.stephenenright.walletwatchlist.web.api.asset.domain.BlockchainAsset;
import com.github.stephenenright.walletwatchlist.web.api.common.models.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "wallet_asset", uniqueConstraints = @UniqueConstraint(columnNames = {"wallet_id", "blockchain_asset_id"}))
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class WalletAsset extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "wallet_id", nullable = false)
	private Wallet wallet;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "blockchain_asset_id", nullable = false)
	private BlockchainAsset blockchainAsset;

	@Column(nullable = false, precision = 36, scale = 18)
	private BigDecimal quantity;
}
