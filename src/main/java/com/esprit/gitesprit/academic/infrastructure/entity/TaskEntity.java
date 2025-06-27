package com.esprit.gitesprit.academic.infrastructure.entity;

import com.esprit.gitesprit.shared.AbstractAuditingEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "tasks")
public class TaskEntity extends AbstractAuditingEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String description;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "mark")
    private Double mark;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(name = "done")
    private boolean done;

    @Column(name = "branch_link")
    private String branchLink;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_student_id", nullable = false)
    private GroupStudentEntity groupStudent;
}
