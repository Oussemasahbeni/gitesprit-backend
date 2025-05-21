package com.esprit.gitesprit.git.infrastructure.entity;

import com.esprit.gitesprit.shared.AbstractAuditingEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Table(name = "git_repository")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class GitRepositoryEntity extends AbstractAuditingEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "repository_name", nullable = false, unique = true)
    private String repositoryName;

    @Column(name = "repository_path", nullable = false)
    private String repositoryPath;
}
