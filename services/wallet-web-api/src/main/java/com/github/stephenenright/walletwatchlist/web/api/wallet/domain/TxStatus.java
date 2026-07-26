/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.wallet.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TxStatus {
	PENDING("Transaction submitted, awaiting confirmation"), SUCCESS("Transaction confirmed successfully"), FAILED(
			"Transaction failed or reverted");

	private final String description;
}
