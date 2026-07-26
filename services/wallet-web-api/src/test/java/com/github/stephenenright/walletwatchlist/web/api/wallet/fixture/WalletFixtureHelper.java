package com.github.stephenenright.walletwatchlist.web.api.wallet.fixture;

import com.github.stephenenright.walletwatchlist.web.api.asset.domain.BlockChain;
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.Wallet;
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WalletStatus;
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WalletSyncStatus;
import java.math.BigDecimal;
import java.time.Instant;

public final class WalletFixtureHelper {

	private WalletFixtureHelper() {
	}

	public static Wallet createWallet(String address, BlockChain blockChain) {
		return Wallet.builder().address(address).blockChain(blockChain).status(WalletStatus.ACTIVE)
				.syncStatus(WalletSyncStatus.SYNCED).syncRetryCount(0).balanceUsd(BigDecimal.ZERO)
				.dateCreated(Instant.now()).dateUpdated(Instant.now()).build();
	}

	public static Wallet createWallet(String address, BlockChain blockChain, WalletStatus status) {
		return Wallet.builder().address(address).blockChain(blockChain).status(status)
				.syncStatus(WalletSyncStatus.SYNCED).syncRetryCount(0).balanceUsd(BigDecimal.ZERO)
				.dateCreated(Instant.now()).dateUpdated(Instant.now()).build();
	}

	public static Wallet createSampleWallet1(BlockChain blockChain) {
		return createWallet("0x742d35Cc6634C0532925a3b844Bc454e4438f44e", blockChain);
	}

	public static Wallet createSampleWallet2(BlockChain blockChain) {
		return createWallet("0x8ba1f109551bD432803012645Ac136ddd64DBA72", blockChain);
	}

	public static Wallet createSampleWallet3(BlockChain blockChain) {
		return createWallet("0xdD870fA1b7C4700F2BD7f44238821C26f7392148", blockChain);
	}
}
