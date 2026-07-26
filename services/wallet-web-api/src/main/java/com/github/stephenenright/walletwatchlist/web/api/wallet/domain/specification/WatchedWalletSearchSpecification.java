/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.wallet.domain.specification;

import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WatchedWallet;
import com.github.stephenenright.walletwatchlist.web.api.wallet.dto.request.SearchWatchedWalletRequest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@RequiredArgsConstructor
public class WatchedWalletSearchSpecification implements Specification<WatchedWallet> {

	private final SearchWatchedWalletRequest searchRequest;

	@Override
	public Predicate toPredicate(Root<WatchedWallet> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		List<Predicate> predicates = new ArrayList<>();

		if (searchRequest.getWatcherId() != null) {
			predicates.add(cb.equal(root.get("watcher").get("id"), searchRequest.getWatcherId()));
		}

		if (searchRequest.getWalletId() != null) {
			predicates.add(cb.equal(root.get("wallet").get("id"), searchRequest.getWalletId()));
		}

		if (searchRequest.getStatus() != null) {
			predicates.add(cb.equal(root.get("status"), searchRequest.getStatus()));
		}

		if (searchRequest.getWalletStatus() != null) {
			predicates.add(cb.equal(root.get("wallet").get("status"), searchRequest.getWalletStatus()));
		}

		return cb.and(predicates.toArray(new Predicate[0]));
	}

	public static Specification<WatchedWallet> fromRequest(SearchWatchedWalletRequest request) {
		return new WatchedWalletSearchSpecification(request);
	}
}
