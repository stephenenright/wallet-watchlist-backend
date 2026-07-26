/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.wallet.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActivityType {
	TRANSFER_IN("Received tokens or native currency"), TRANSFER_OUT("Sent tokens or native currency"), SWAP(
			"Exchanged one token for another"), MINT("Minted new tokens or NFT"), BURN("Burned tokens"), APPROVE(
					"Approved token spending"), CONTRACT_INTERACTION("Interacted with a smart contract"), STAKE(
							"Staked tokens"), UNSTAKE("Unstaked tokens"), CLAIM("Claimed rewards");

	private final String description;
}
