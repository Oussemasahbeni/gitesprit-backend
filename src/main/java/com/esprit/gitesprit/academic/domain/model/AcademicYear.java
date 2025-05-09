package com.esprit.gitesprit.academic.domain.model;

import com.esprit.gitesprit.shared.AbstractAuditingModel;
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
public class AcademicYear extends AbstractAuditingModel {
    private UUID id;
    private int startYear;
    private int endYear;
    private Set<Classroom> classrooms;

    public void addClass(Classroom classroom) {
        if (classrooms == null) {
            classrooms = new HashSet<>();
        }
        classrooms.add(classroom);
    }

    public void removeClass(Classroom classroom) {
        if (classrooms != null) {
            classrooms.remove(classroom);
        }
    }
}
