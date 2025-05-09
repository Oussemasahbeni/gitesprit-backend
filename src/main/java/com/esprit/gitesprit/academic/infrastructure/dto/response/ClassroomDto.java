package com.esprit.gitesprit.academic.infrastructure.dto.response;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/** DTO for {@link com.esprit.gitesprit.academic.infrastructure.entity.ClassroomEntity} */
public record ClassroomDto(
        String createdBy,
        String lastModifiedBy,
        Instant createdAt,
        Instant updatedAt,
        Short version,
        UUID id,
        String name,
        AcademicYearDto academicYear,
        List<SubjectEntityDto> subjects)
    implements Serializable {}
