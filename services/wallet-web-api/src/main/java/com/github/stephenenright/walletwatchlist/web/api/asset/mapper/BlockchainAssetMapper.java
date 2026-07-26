/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.asset.mapper;

import com.github.stephenenright.walletwatchlist.web.api.asset.domain.BlockchainAsset;
import com.github.stephenenright.walletwatchlist.web.api.asset.dto.BlockchainAssetDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CurrencyMapper.class, BlockChainMapper.class})
public interface BlockchainAssetMapper {

	@Mapping(source = "native", target = "nativeAsset")
	BlockchainAssetDTO toDto(BlockchainAsset entity);

	List<BlockchainAssetDTO> toDtoList(List<BlockchainAsset> entities);

	@Mapping(source = "nativeAsset", target = "isNative")
	BlockchainAsset toEntity(BlockchainAssetDTO dto);
}
