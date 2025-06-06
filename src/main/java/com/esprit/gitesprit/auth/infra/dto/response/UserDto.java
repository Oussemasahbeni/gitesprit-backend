package com.esprit.gitesprit.auth.infra.dto.response;

import com.esprit.gitesprit.auth.domain.enums.Locale;
import com.esprit.gitesprit.auth.domain.enums.RoleType;

import java.time.Instant;
import java.util.List;

public record UserDto(
        String id,
        String email,
        String firstName,
        String lastName,
        String fullName,
        String username,
        List<RoleType> roleTypes,
        Locale locale,
        String phoneNumber,
        String profilePicture,
        Boolean enabled,
        Boolean emailVerified,
        Instant createdAt) {}
