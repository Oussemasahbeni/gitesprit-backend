package com.esprit.gitesprit.auth.infra.mapper;

import com.esprit.gitesprit.auth.domain.model.AuthUser;
import com.esprit.gitesprit.auth.infra.dto.request.UpdateUserRequestDto;
import com.esprit.gitesprit.auth.infra.dto.response.UserDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class DtoMapper {


    public abstract UserDto toUserDto(AuthUser user);
    
    public abstract AuthUser toAuthUser(UpdateUserRequestDto userRequestDto);
    
    public abstract List<UserDto> toUserDtoList(List<AuthUser> users);
}
