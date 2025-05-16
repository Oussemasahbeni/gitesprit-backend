package com.esprit.gitesprit.academic.infrastructure.dto.response;

import java.time.Instant;
import java.util.UUID;

public record AcademicYearSimpleDto(
        String createdBy,
        String lastModifiedBy,
        Instant createdAt,
        Instant updatedAt,
        Short version,
        UUID id,
        int startYear,
        int endYear
) {}
