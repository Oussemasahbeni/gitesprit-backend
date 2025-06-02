package com.esprit.gitesprit.git.infrastructure.repository;

import com.esprit.gitesprit.git.infrastructure.entity.GitRepositoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GitRepositoryRepository extends JpaRepository<GitRepositoryEntity, UUID> {

    Optional<GitRepositoryEntity> findByRepositoryName(String name);

    Optional<GitRepositoryEntity> findByRepositoryPath(String filesystemPath);

    List<GitRepositoryEntity> findAllByGroupId(UUID groupId);
}
