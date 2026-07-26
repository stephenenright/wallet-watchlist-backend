/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.wallet.domain;

import com.github.stephenenright.walletwatchlist.web.api.asset.domain.BlockchainAsset;
import com.github.stephenenright.walletwatchlist.web.api.common.models.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "wallet_transaction", uniqueConstraints = @UniqueConstraint(columnNames = {"wallet_id", "tx_hash"}))
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class WalletTransaction extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "wallet_id", nullable = false)
	private Wallet wallet;

	@Column(name = "tx_hash", nullable = false)
	private String txHash;

	@Column(name = "block_number")
	private Long blockNumber;

	@Column(name = "from_address", nullable = false)
	private String fromAddress;

	@Column(name = "to_address")
	private String toAddress;

	@Column(name = "tx_value", precision = 36, scale = 18)
	private BigDecimal txValue;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "blockchain_asset_id")
	private BlockchainAsset blockchainAsset;

	@Column(name = "gas_used", precision = 36, scale = 18)
	private BigDecimal gasUsed;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TxStatus status;

	@Column(name = "date_occurred", nullable = false)
	private Instant dateOccurred;
}
