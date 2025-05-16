package com.esprit.gitesprit.academic.infrastructure.dto.response;

import com.esprit.gitesprit.auth.infra.dto.response.UserDto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record GroupSimpleDto(
        String createdBy,
        String lastModifiedBy,
        Instant createdAt,
        Instant updatedAt,
        Short version,
        UUID id,
        String name,
        List<UserDto> students){}
