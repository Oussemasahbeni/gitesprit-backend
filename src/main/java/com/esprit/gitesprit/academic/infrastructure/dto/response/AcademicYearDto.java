package com.esprit.gitesprit.academic.infrastructure.dto.response;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record AcademicYearDto(
        String createdBy,
        String lastModifiedBy,
        Instant createdAt,
        Instant updatedAt,
        Short version,
        UUID id,
        int startYear,
        int endYear,
        Set<ClassroomDto> classrooms){}
