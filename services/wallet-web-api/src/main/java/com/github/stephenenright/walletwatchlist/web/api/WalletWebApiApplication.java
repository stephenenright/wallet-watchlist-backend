package com.github.stephenenright.walletwatchlist.web.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.github.stephenenright.walletwatchlist.*")
public class WalletWebApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(WalletWebApiApplication.class, args);
	}
}
