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
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Group extends AbstractAuditingModel {
    private UUID id;
    private String name;
    private Subject subject;
    private Set<User> students;
    private String githubRepoFullName;

    public void addStudent(User student) {
        if (students == null) {
            students = new HashSet<>();
        }
        students.add(student);
    }

    public void removeStudent(User student) {
        if (students != null) {
            students.remove(student);
        }
    }
}
