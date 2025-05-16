package com.esprit.gitesprit.academic.infrastructure.dto.response;

import com.esprit.gitesprit.auth.infra.dto.response.UserDto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record SubjectDto(
        String createdBy,
        String lastModifiedBy,
        Instant createdAt,
        Instant updatedAt,
        Short version,
        UUID id,
        String name,
        UserDto teacher,
        ClassroomSimpleDto classroom,
        List<GroupSimpleDto> groups
) {}
