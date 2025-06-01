package com.esprit.gitesprit.git.infrastructure.dto;

import com.esprit.gitesprit.academic.infrastructure.dto.response.GroupDto;

import java.util.UUID;

public record RepositoryDto(
        UUID id,
        String repositoryName,
        String repositoryPath,
        GroupDto group
) {
}
