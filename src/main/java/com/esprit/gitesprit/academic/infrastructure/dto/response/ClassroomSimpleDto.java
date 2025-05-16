package com.esprit.gitesprit.academic.infrastructure.dto.response;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ClassroomSimpleDto(
        String createdBy,
        String lastModifiedBy,
        Instant createdAt,
        Instant updatedAt,
        Short version,
        UUID id,
        String name)
    implements Serializable {}
