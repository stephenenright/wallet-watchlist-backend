/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.wallet.mapper;

import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WalletActivity;
import com.github.stephenenright.walletwatchlist.web.api.wallet.dto.WalletActivityDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WalletActivityMapper {

	@Mapping(source = "transaction.txHash", target = "txHash")
	WalletActivityDTO toDto(WalletActivity entity);

	List<WalletActivityDTO> toDtoList(List<WalletActivity> entities);
}
