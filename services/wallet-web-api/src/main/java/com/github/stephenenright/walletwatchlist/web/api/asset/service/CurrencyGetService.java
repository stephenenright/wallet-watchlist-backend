/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.asset.service;

import com.github.stephenenright.walletwatchlist.web.api.asset.domain.Currency;
import com.github.stephenenright.walletwatchlist.web.api.asset.repository.CurrencyRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CurrencyGetService {

	private static final Sort BY_SYMBOL = Sort.by(Sort.Direction.ASC, "symbol");

	private final CurrencyRepository currencyRepository;

	public CurrencyGetService(CurrencyRepository currencyRepository) {
		this.currencyRepository = currencyRepository;
	}

	public Page<Currency> getAll(Pageable pageable) {
		Pageable sorted = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), BY_SYMBOL);
		return currencyRepository.findAll(sorted);
	}

	public Optional<Currency> getBySymbol(String symbol) {
		return currencyRepository.findBySymbol(symbol);
	}
}
