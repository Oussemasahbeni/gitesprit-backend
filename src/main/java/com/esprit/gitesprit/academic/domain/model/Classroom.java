package com.esprit.gitesprit.academic.domain.model;

import com.esprit.gitesprit.shared.AbstractAuditingModel;
import com.esprit.gitesprit.users.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Classroom extends AbstractAuditingModel {
    private UUID id;
    private String name;
    private AcademicYear academicYear;
    private List<Subject> subjects;

    public void addSubject(Subject subject) {
        if (subjects == null) {
            subjects = new ArrayList<>();
        }
        subjects.add(subject);
    }

    public void removeSubject(Subject subject) {
        if (subjects != null) {
            subjects.remove(subject);
        }
    }
}
