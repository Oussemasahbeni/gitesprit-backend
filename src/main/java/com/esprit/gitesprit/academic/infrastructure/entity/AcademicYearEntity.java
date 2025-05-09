package com.esprit.gitesprit.academic.infrastructure.entity;

import com.esprit.gitesprit.shared.AbstractAuditingEntity;
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
@Table(name = "academic_years")
public class AcademicYearEntity extends AbstractAuditingEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private int startYear;

    @Column(nullable = false)
    private int endYear;

    @OneToMany(mappedBy = "academicYear", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ClassroomEntity> classrooms = new HashSet<>();
}