/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.wallet.service;

public class WalletNotFoundException extends RuntimeException {

	public WalletNotFoundException(String message) {
		super(message);
	}

	public WalletNotFoundException() {
		super("Wallet not found");
	}
}
