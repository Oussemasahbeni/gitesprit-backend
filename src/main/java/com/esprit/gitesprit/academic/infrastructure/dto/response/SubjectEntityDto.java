package com.esprit.gitesprit.academic.infrastructure.dto.response;

import com.esprit.gitesprit.auth.infra.dto.response.UserDto;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/** DTO for {@link com.esprit.gitesprit.academic.infrastructure.entity.SubjectEntity} */
public record SubjectEntityDto(
        String createdBy,
        String lastModifiedBy,
        Instant createdAt,
        Instant updatedAt,
        Short version,
        UUID id,
        String name,
        ClassroomDto classroom,
        UserDto teacher,
        List<GroupEntityDto> groups)
    implements Serializable {}
