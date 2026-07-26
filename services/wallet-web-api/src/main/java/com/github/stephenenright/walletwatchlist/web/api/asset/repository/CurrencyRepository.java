/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.asset.repository;

import com.github.stephenenright.walletwatchlist.web.api.asset.domain.Currency;
import com.github.stephenenright.walletwatchlist.web.api.common.repository.BaseJpaRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends BaseJpaRepository<Currency, UUID> {

	Optional<Currency> findBySymbol(String symbol);
}
