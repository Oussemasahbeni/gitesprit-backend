package com.esprit.gitesprit.academic.infrastructure.dto.request;

import java.util.List;
import java.util.UUID;

public record UpdateGroupDto(
        UUID id,
        String name,
        UUID subjectId,
        List<UUID> studentIds,
        String githubRepoFullName
) {}
