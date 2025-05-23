package com.esprit.gitesprit.academic.infrastructure.dto.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ClassroomDto(
        String createdBy,
        String lastModifiedBy,
        Instant createdAt,
        Instant updatedAt,
        Short version,
        UUID id,
        String name,
        AcademicYearSimpleDto academicYear,
        List<SubjectSimpleDto> subjects
) {}
