/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.wallet.service;

import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.Wallet;
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WalletActivity;
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WalletAsset;
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WalletSyncStatus;
import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WalletTransaction;
import com.github.stephenenright.walletwatchlist.web.api.wallet.repository.WalletActivityRepository;
import com.github.stephenenright.walletwatchlist.web.api.wallet.repository.WalletAssetRepository;
import com.github.stephenenright.walletwatchlist.web.api.wallet.repository.WalletRepository;
import com.github.stephenenright.walletwatchlist.web.api.wallet.repository.WalletTransactionRepository;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalletCreationDataMockingService {

	private final WalletRepository walletRepository;
	private final WalletAssetRepository walletAssetRepository;
	private final WalletActivityRepository walletActivityRepository;
	private final WalletTransactionRepository walletTransactionRepository;

	@Transactional
	public Wallet mockWalletDataOnCreate(Wallet wallet) {
		Wallet savedWallet = walletRepository.save(wallet);

		findExistingWalletOnSameChain(savedWallet).ifPresent(sourceWallet -> {
			copyAssets(sourceWallet, savedWallet);
			Map<WalletTransaction, WalletTransaction> txMap = copyTransactions(sourceWallet, savedWallet);
			copyActivities(sourceWallet, savedWallet, txMap);
			markAsSynced(savedWallet);
		});

		return savedWallet;
	}

	private Optional<Wallet> findExistingWalletOnSameChain(Wallet newWallet) {
		return walletRepository.findAll().stream().filter(w -> !w.getId().equals(newWallet.getId())
				&& w.getBlockChain().getId().equals(newWallet.getBlockChain().getId())).findFirst();
	}

	private void copyAssets(Wallet source, Wallet target) {
		List<WalletAsset> sourceAssets = walletAssetRepository.findByWalletIdWithAssociations(source.getId());

		for (WalletAsset sourceAsset : sourceAssets) {
			WalletAsset newAsset = WalletAsset.builder().wallet(target)
					.blockchainAsset(sourceAsset.getBlockchainAsset()).quantity(sourceAsset.getQuantity()).build();
			target.getAssets().add(newAsset);
		}
		target.setBalanceUsd(source.getBalanceUsd());
	}

	private Map<WalletTransaction, WalletTransaction> copyTransactions(Wallet source, Wallet target) {
		List<WalletTransaction> sourceTransactions = walletTransactionRepository
				.findByWalletIdOrderByDateOccurredDesc(source.getId(), PageRequest.of(0, 50));

		Map<WalletTransaction, WalletTransaction> oldToNewTxMap = new HashMap<>();

		for (WalletTransaction sourceTx : sourceTransactions) {
			String newTxHash = generateMockTxHash(sourceTx.getTxHash(), target.getAddress());

			WalletTransaction newTx = WalletTransaction.builder().wallet(target).txHash(newTxHash)
					.blockNumber(sourceTx.getBlockNumber())
					.fromAddress(sourceTx.getFromAddress().replace(source.getAddress(), target.getAddress()))
					.toAddress(sourceTx.getToAddress() != null
							? sourceTx.getToAddress().replace(source.getAddress(), target.getAddress())
							: null)
					.txValue(sourceTx.getTxValue()).blockchainAsset(sourceTx.getBlockchainAsset())
					.gasUsed(sourceTx.getGasUsed()).status(sourceTx.getStatus())
					.dateOccurred(sourceTx.getDateOccurred()).build();

			target.getTransactions().add(newTx);
			oldToNewTxMap.put(sourceTx, newTx);
		}

		return oldToNewTxMap;
	}

	private void copyActivities(Wallet source, Wallet target, Map<WalletTransaction, WalletTransaction> txMap) {
		List<WalletActivity> sourceActivities = walletActivityRepository
				.findByWalletIdOrderByDateOccurredDesc(source.getId(), PageRequest.of(0, 50));

		for (WalletActivity sourceActivity : sourceActivities) {
			WalletTransaction newTx = null;
			if (sourceActivity.getTransaction() != null) {
				newTx = txMap.get(sourceActivity.getTransaction());
			}

			WalletActivity newActivity = WalletActivity.builder().wallet(target).transaction(newTx)
					.activityType(sourceActivity.getActivityType())
					.summary(sourceActivity.getSummary().replace(source.getAddress(), target.getAddress()))
					.valueUsd(sourceActivity.getValueUsd()).dateOccurred(sourceActivity.getDateOccurred()).build();
			target.getActivities().add(newActivity);
		}
	}

	private String generateMockTxHash(String originalHash, String walletAddress) {
		String addressSuffix = walletAddress.length() > 8
				? walletAddress.substring(walletAddress.length() - 8)
				: walletAddress;
		return originalHash.substring(0, originalHash.length() - 8) + addressSuffix;
	}

	private void markAsSynced(Wallet wallet) {
		wallet.setSyncStatus(WalletSyncStatus.SYNCED);
		wallet.setDateLastSynced(Instant.now());
		wallet.getActivities().stream().map(WalletActivity::getDateOccurred).max(Instant::compareTo)
				.ifPresent(wallet::setDateLastActivity);
		walletRepository.save(wallet);
	}
}
