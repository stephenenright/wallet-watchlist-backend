/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.wallet.mapper;

import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.Wallet;
import com.github.stephenenright.walletwatchlist.web.api.wallet.dto.WalletDTO;
import com.github.stephenenright.walletwatchlist.web.api.wallet.dto.WalletDetailDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WalletMapper {

	@Mapping(source = "blockChain.id", target = "blockChainId")
	@Mapping(source = "blockChain.code", target = "blockChainCode")
	@Mapping(source = "blockChain.name", target = "blockChainName")
	WalletDTO toDto(Wallet entity);

	List<WalletDTO> toDtoList(List<Wallet> entities);

	@Mapping(source = "blockChain.id", target = "blockChainId")
	@Mapping(source = "blockChain.code", target = "blockChainCode")
	@Mapping(source = "blockChain.name", target = "blockChainName")
	@Mapping(target = "assets", ignore = true)
	@Mapping(target = "recentActivity", ignore = true)
	WalletDetailDTO toDetailDto(Wallet entity);
}
