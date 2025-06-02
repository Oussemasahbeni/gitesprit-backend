package com.esprit.gitesprit.academic.domain.model;

import com.esprit.gitesprit.git.domain.model.GitRepository;
import com.esprit.gitesprit.shared.AbstractAuditingModel;
import com.esprit.gitesprit.users.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Group extends AbstractAuditingModel {
    private UUID id;
    private String name;
    private Subject subject;
    private Set<GroupStudent> students;
    private Set<GitRepository> repositories;
    private Double mark;
    private String comment;

    public void addGroupStudent(GroupStudent groupStudent) {
        if (students == null) {
            students = new HashSet<>();
        }
        students.add(groupStudent);
    }

    public void removeGroupStudent(GroupStudent groupStudent) {
        if (students != null) {
            students.remove(groupStudent);
        }
    }

    public void addStudent(User student) {
        GroupStudent groupStudent = new GroupStudent();
        groupStudent.setStudent(student);
        groupStudent.setGroup(this);
        addGroupStudent(groupStudent);
    }

    public void removeStudent(User student) {
        if (students != null) {
            students.removeIf(gs -> gs.getStudent().getId().equals(student.getId()));
        }
    }



    public void addRepository(GitRepository repository) {
        if(repositories == null) {
            repositories = new HashSet<>();
        }
        repositories.add(repository);
    }

    public void removeRepository(GitRepository repository) {
        if(repositories != null) {
            repositories.remove(repository);
        }
    }
}
