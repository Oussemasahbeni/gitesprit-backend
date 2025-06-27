package com.esprit.gitesprit.academic.infrastructure.dto.response;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for {@link com.esprit.gitesprit.academic.infrastructure.entity.TaskEntity}
 */
public record TaskDto(
        UUID id,
        String createdBy,
        String lastModifiedBy,
        Instant createdAt,
        Instant updatedAt,
        Short version,
        String description,
        LocalDate dueDate,
        Double mark,
        String comment,
        boolean done,
        String branchLink
) {
}