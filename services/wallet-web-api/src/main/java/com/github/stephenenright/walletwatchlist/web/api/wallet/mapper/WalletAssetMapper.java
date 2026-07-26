/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.wallet.mapper;

import com.github.stephenenright.walletwatchlist.web.api.wallet.domain.WalletAsset;
import com.github.stephenenright.walletwatchlist.web.api.wallet.dto.WalletAssetDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WalletAssetMapper {

	@Mapping(source = "blockchainAsset.currency.symbol", target = "currencySymbol")
	@Mapping(source = "blockchainAsset.currency.name", target = "currencyName")
	@Mapping(source = "blockchainAsset.native", target = "isNative")
	WalletAssetDTO toDto(WalletAsset entity);

	List<WalletAssetDTO> toDtoList(List<WalletAsset> entities);
}
