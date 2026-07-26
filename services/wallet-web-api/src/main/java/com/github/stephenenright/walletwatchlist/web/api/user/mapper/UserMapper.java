/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.user.mapper;

import com.github.stephenenright.walletwatchlist.web.api.user.domain.User;
import com.github.stephenenright.walletwatchlist.web.api.user.dto.UserDTO;
import com.github.stephenenright.walletwatchlist.web.api.user.dto.request.CreateUserRequestDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

	UserDTO toDto(User entity);

	List<UserDTO> toDtoList(List<User> entities);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "dateCreated", ignore = true)
	@Mapping(target = "dateUpdated", ignore = true)
	User toEntity(CreateUserRequestDTO dto);
}
