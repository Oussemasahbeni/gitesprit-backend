package com.esprit.gitesprit.academic.infrastructure.entity;

import com.esprit.gitesprit.git.domain.model.GitRepository;
import com.esprit.gitesprit.git.infrastructure.entity.GitRepositoryEntity;
import com.esprit.gitesprit.shared.AbstractAuditingEntity;
import com.esprit.gitesprit.users.infrastructure.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "groups")
public class GroupEntity extends AbstractAuditingEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    @JsonBackReference
    private SubjectEntity subject;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<GroupStudentEntity> students = new HashSet<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<GitRepositoryEntity> repositories = new HashSet<>();

    @Column(name = "marks")
    private Double mark;

    @Column(name = "comments", columnDefinition = "TEXT")
    private String comment;

}

