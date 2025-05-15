package com.esprit.gitesprit.academic.domain.service;

import com.esprit.gitesprit.academic.domain.model.Group;
import com.esprit.gitesprit.academic.domain.model.Subject;
import com.esprit.gitesprit.academic.domain.port.input.GroupUseCases;
import com.esprit.gitesprit.academic.domain.port.input.SubjectUseCases;
import com.esprit.gitesprit.academic.domain.port.output.Groups;
import com.esprit.gitesprit.academic.domain.port.output.Subjects;
import com.esprit.gitesprit.exception.NotFoundException;
import com.esprit.gitesprit.shared.annotation.DomainService;
import com.esprit.gitesprit.users.domain.model.User;
import com.esprit.gitesprit.users.domain.port.output.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@DomainService
@RequiredArgsConstructor
public class GroupService implements GroupUseCases {
    private final Groups groups;
    private final Users users;
    private final Subjects subjects;

    @Override
    public Group create(Group group, UUID subjectId) {
        Subject subject = subjects.findById(subjectId).orElseThrow(
                () -> new NotFoundException(NotFoundException.NotFoundExceptionType.SUBJECT_NOT_FOUND)
        );
        group.setSubject(subject);
        return groups.create(group);
    }

    @Override
    public Group update(Group group) {
        findById(group.getId());
        return groups.update(group);
    }

    @Override
    public Group assignStudent(UUID groupId, UUID studentId) {
        Group group = findById(groupId);
        User student = users.findById(studentId).orElseThrow(
                () -> new NotFoundException(NotFoundException.NotFoundExceptionType.USER_NOT_FOUND)
        );
        if (group.getStudents().contains(student)) {
            return group;
        }
        group.addStudent(student);
        groups.update(group);
        return group;
    }

    @Override
    public Group removeStudent(UUID groupId, UUID studentId) {
        Group group = findById(groupId);
        User student = users.findById(studentId).orElseThrow(
                () -> new NotFoundException(NotFoundException.NotFoundExceptionType.USER_NOT_FOUND)
        );
        if (!checkStudentInGroup(group, studentId)) {
            return group;
        }
        group.removeStudent(student);
        groups.update(group);
        return group;
    }

    @Override
    public Group assignStudents(UUID groupId, List<UUID> studentIds) {
        Group group = findById(groupId);
        for (UUID studentId : studentIds) {
            User student = users.findById(studentId).orElseThrow(
                    () -> new NotFoundException(NotFoundException.NotFoundExceptionType.USER_NOT_FOUND)
            );
            if (!group.getStudents().contains(student)) {
                group.addStudent(student);
            }
        }
        groups.update(group);
        return group;
    }

    @Override
    public Group findById(UUID id) {
        return groups.findById(id).orElseThrow(() -> new NotFoundException(NotFoundException.NotFoundExceptionType.GROUP_NOT_FOUND));
    }

    @Override
    public void deleteById(UUID id) {
        findById(id);
        groups.deleteById(id);
    }

    @Override
    public void deleteAll() {
        groups.deleteAll();
    }

    @Override
    public List<Group> findAll() {
        return groups.findAll();
    }

    @Override
    public Page<Group> findAllPaginated(String search, Pageable pageable) {
        return groups.findAllPaginated(search, pageable);
    }

    private boolean checkStudentInGroup(Group group, UUID studentId) {
        return group.getStudents().stream().anyMatch(student -> student.getId().equals(studentId));
    }
}