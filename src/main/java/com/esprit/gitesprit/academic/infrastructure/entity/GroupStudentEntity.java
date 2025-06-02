package com.esprit.gitesprit.academic.infrastructure.entity;

import com.esprit.gitesprit.shared.AbstractAuditingEntity;
import com.esprit.gitesprit.users.infrastructure.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;
import com.esprit.gitesprit.shared.AbstractAuditingEntity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "group_students",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"student_id", "group_id"}) // A student can only have one mark per group
        })
public class GroupStudentEntity extends AbstractAuditingEntity {


    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @JsonBackReference()
    private UserEntity student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    @JsonBackReference()
    private GroupEntity group;

    @Column(name = "individual_mark")
    private Double individualMark;

    @Column(name = "individual_comment", columnDefinition = "TEXT")
    private String individualComment;

    @Transient
    private Double finalStudentMarkInGroup;

    public Double calculateFinalStudentMarkInGroup() {
        if (group == null || group.getSubject() == null || individualMark == null || group.getMark() == null) {
            return null;
        }
        SubjectEntity subject = group.getSubject();
        Double groupContribution = (group.getMark() * subject.getGroupMarkPercentage()) / 100.0;
        Double individualContribution = (this.individualMark * subject.getIndividualMarkPercentage()) / 100.0;

        if (subject.getGroupMarkPercentage() + subject.getIndividualMarkPercentage() != 100.0) {
            System.err.println("Warning: Subject percentages do not sum to 100 for subject: " + subject.getName());
        }

        this.finalStudentMarkInGroup = groupContribution + individualContribution;
        return this.finalStudentMarkInGroup;
    }
}
