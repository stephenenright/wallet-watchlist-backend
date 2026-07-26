/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.wallet.mapper;

import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WatchedWallet;
import com.github.stephenenright.walletwatchlist.web.api.wallet.dto.WatchedWalletDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WatchedWalletMapper {

	@Mapping(source = "id", target = "id")
	@Mapping(source = "watcher.id", target = "watcherId")
	@Mapping(source = "watcher.email", target = "watcherEmail")
	@Mapping(source = "wallet.id", target = "walletId")
	@Mapping(source = "wallet.address", target = "walletAddress")
	@Mapping(source = "wallet.blockChain.code", target = "walletBlockChainCode")
	WatchedWalletDTO toDto(WatchedWallet entity);

	List<WatchedWalletDTO> toDtoList(List<WatchedWallet> entities);
}
