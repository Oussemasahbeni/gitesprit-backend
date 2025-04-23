package com.esprit.gitesprit.auth.infra.mapper;

import com.esprit.gitesprit.auth.domain.model.AuthUser;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.*;

import java.time.Instant;

@Mapper(componentModel = "spring")
public abstract class AuthMapper {


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract AuthUser partialUpdate(AuthUser user, @MappingTarget AuthUser authUser);

    public abstract UserRepresentation toUserRepresentation(AuthUser user);

    @Mapping(target = "createdAt", source = "createdTimestamp", qualifiedByName = "getCreatedTimeStamp")
    public abstract AuthUser toAuthUser(UserRepresentation userRepresentation);

    @Named("getCreatedTimeStamp")
    public Instant getCreatedTimeStamp(Long createdTimestamp) {
        return Instant.ofEpochMilli(createdTimestamp);
    }
}
