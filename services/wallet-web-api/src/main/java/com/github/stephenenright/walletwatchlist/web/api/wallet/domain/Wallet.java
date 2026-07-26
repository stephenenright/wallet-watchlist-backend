/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.wallet.domain;

import com.github.stephenenright.walletwatchlist.web.api.asset.domain.BlockChain;
import com.github.stephenenright.walletwatchlist.web.api.common.models.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "wallet", uniqueConstraints = @UniqueConstraint(columnNames = {"address", "blockchain_id"}))
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Wallet extends BaseEntity {

	@Column(nullable = false)
	private String address;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "blockchain_id", nullable = false)
	private BlockChain blockChain;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private WalletStatus status;

	@Enumerated(EnumType.STRING)
	@Column(name = "sync_status", nullable = false)
	private WalletSyncStatus syncStatus;

	@Column(name = "sync_retry_count", nullable = false)
	private int syncRetryCount;

	@Column(name = "date_last_synced")
	private Instant dateLastSynced;

	@Column(name = "date_last_activity")
	private Instant dateLastActivity;

	@Builder.Default
	@Column(name = "balance_usd", precision = 18, scale = 2)
	private BigDecimal balanceUsd = BigDecimal.ZERO;

	@OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@Builder.Default
	private List<WalletAsset> assets = new ArrayList<>();

	@OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@Builder.Default
	private List<WalletActivity> activities = new ArrayList<>();

	@OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@Builder.Default
	private List<WalletTransaction> transactions = new ArrayList<>();
}
