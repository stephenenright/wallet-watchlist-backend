/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.wallet.domain;

import com.github.stephenenright.walletwatchlist.web.api.common.models.entity.BaseEntity;
import com.github.stephenenright.walletwatchlist.web.api.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "watched_wallet", uniqueConstraints = @UniqueConstraint(columnNames = {"watcher_id", "wallet_id"}))
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class WatchedWallet extends BaseEntity {

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "watcher_id", nullable = false)
	private User watcher;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "wallet_id", nullable = false)
	private Wallet wallet;

	@Column
	private String label;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private WatchedWalletStatus status;
}
