/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.asset.repository;

import com.github.stephenenright.walletwatchlist.web.api.asset.domain.BlockChain;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockChainRepository extends JpaRepository<BlockChain, UUID> {

	Optional<BlockChain> findByCode(String code);

	boolean existsByCode(String code);
}
