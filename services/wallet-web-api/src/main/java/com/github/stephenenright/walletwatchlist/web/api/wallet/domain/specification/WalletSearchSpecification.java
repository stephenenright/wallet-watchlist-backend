/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.wallet.domain.specification;

import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.Wallet;
import com.github.stephenenright.walletwatchlist.web.api.wallet.dto.request.SearchWalletRequest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@RequiredArgsConstructor
public class WalletSearchSpecification implements Specification<Wallet> {

	private final SearchWalletRequest searchRequest;

	@Override
	public Predicate toPredicate(Root<Wallet> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		List<Predicate> predicates = new ArrayList<>();

		if (searchRequest.getStatus() != null) {
			predicates.add(cb.equal(root.get("status"), searchRequest.getStatus()));
		}

		return cb.and(predicates.toArray(new Predicate[0]));
	}

	public static Specification<Wallet> fromRequest(SearchWalletRequest request) {
		return new WalletSearchSpecification(request);
	}
}
