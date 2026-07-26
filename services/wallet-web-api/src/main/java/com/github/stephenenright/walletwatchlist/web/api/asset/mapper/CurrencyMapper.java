/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.asset.mapper;

import com.github.stephenenright.walletwatchlist.web.api.asset.domain.Currency;
import com.github.stephenenright.walletwatchlist.web.api.asset.dto.CurrencyDTO;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {

	CurrencyDTO toDto(Currency entity);

	List<CurrencyDTO> toDtoList(List<Currency> entities);

	Currency toEntity(CurrencyDTO dto);
}
