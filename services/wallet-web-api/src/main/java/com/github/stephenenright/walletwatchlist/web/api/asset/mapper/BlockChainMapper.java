/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.asset.mapper;

import com.github.stephenenright.walletwatchlist.web.api.asset.domain.BlockChain;
import com.github.stephenenright.walletwatchlist.web.api.asset.dto.BlockChainDTO;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BlockChainMapper {

	BlockChainDTO toDto(BlockChain entity);

	List<BlockChainDTO> toDtoList(List<BlockChain> entities);

	BlockChain toEntity(BlockChainDTO dto);
}
