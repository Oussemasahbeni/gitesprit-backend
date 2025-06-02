package com.esprit.gitesprit.academic.infrastructure.dto.response;

import com.esprit.gitesprit.auth.infra.dto.response.UserDto;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record SubjectSimpleDto(
        String createdBy,
        String lastModifiedBy,
        Instant createdAt,
        Instant updatedAt,
        Short version,
        UUID id,
        String name,
        Double groupMarkPercentage,
        Double individualMarkPercentage,
        UserDto teacher) {}
