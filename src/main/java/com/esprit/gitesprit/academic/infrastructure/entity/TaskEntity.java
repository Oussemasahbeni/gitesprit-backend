package com.esprit.gitesprit.academic.infrastructure.entity;

import com.esprit.gitesprit.shared.AbstractAuditingEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;
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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "task_links", joinColumns = @JoinColumn(name = "task_id"))
    @Column(name = "link")
    private List<String> branchLinks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_student_id", nullable = false)
    private GroupStudentEntity groupStudent;
}
