package com.esprit.gitesprit.git.infrastructure.entity;

import com.esprit.gitesprit.academic.infrastructure.entity.GroupEntity;
import com.esprit.gitesprit.academic.infrastructure.entity.SubjectEntity;
import com.esprit.gitesprit.shared.AbstractAuditingEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    @JsonBackReference
    private GroupEntity group;
}
