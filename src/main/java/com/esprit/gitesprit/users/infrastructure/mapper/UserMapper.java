package com.esprit.gitesprit.users.infrastructure.mapper;

import com.esprit.gitesprit.users.infrastructure.dto.request.UserRequestDto;
import com.esprit.gitesprit.shared.mapstruct.CycleAvoidingMappingContext;
import com.esprit.gitesprit.shared.mapstruct.DoIgnore;
import com.esprit.gitesprit.users.domain.model.Role;
import com.esprit.gitesprit.users.domain.model.User;
import com.esprit.gitesprit.users.infrastructure.dto.request.UpdateProfileRequest;
import com.esprit.gitesprit.users.infrastructure.dto.response.UserDto;
import com.esprit.gitesprit.users.infrastructure.entity.RoleEntity;
import com.esprit.gitesprit.users.infrastructure.entity.UserEntity;
import org.mapstruct.*;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = IGNORE)
public abstract class UserMapper {

    public abstract UserEntity mapToUserEntity(
            User user, @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    public abstract User mapToUser(
            UserEntity userEntity, @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);


    @DoIgnore
    public UserEntity toUserEntity(User user) {
        return mapToUserEntity(user, new CycleAvoidingMappingContext());
    }
    @DoIgnore
    public User toUser(UserEntity userEntity) {
        return mapToUser(userEntity, new CycleAvoidingMappingContext());
    }


    public abstract Role toRole(
            RoleEntity roleEntity, @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    public abstract RoleEntity toRoleEntity(
            Role role, @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @DoIgnore
    public Role toRole(RoleEntity roleEntity) {
        return toRole(roleEntity, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    public RoleEntity toRoleEntity(Role role) {
        return toRoleEntity(role, new CycleAvoidingMappingContext());
    }


    public abstract User toAuthUser(UserRequestDto userRequestDto);





    public abstract UserDto toUserDto(User user);

    @Mapping(target = "id", ignore = true)
    public abstract UserEntity updateUserFromProfileUpdateRequest(
            UpdateProfileRequest updateProfileRequest, @MappingTarget UserEntity user);
}
