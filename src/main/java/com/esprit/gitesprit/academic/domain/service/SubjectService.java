package com.esprit.gitesprit.academic.domain.service;

import com.esprit.gitesprit.academic.domain.model.Classroom;
import com.esprit.gitesprit.academic.domain.model.Group;
import com.esprit.gitesprit.academic.domain.model.Subject;
import com.esprit.gitesprit.academic.domain.port.input.SubjectUseCases;
import com.esprit.gitesprit.academic.domain.port.output.Classrooms;
import com.esprit.gitesprit.academic.domain.port.output.Groups;
import com.esprit.gitesprit.academic.domain.port.output.Subjects;
import com.esprit.gitesprit.exception.NotFoundException;
import com.esprit.gitesprit.shared.annotation.DomainService;
import com.esprit.gitesprit.users.domain.model.User;

import java.util.List;
import java.util.UUID;

import com.esprit.gitesprit.users.domain.port.output.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@DomainService
@RequiredArgsConstructor
public class SubjectService implements SubjectUseCases {
    private final Subjects subjects;
    private final Users users;
    private final Groups groups;
    private final Classrooms classrooms;

    @Override
    public Subject create(Subject subject, UUID teacherId, UUID classroomId) {
        User teacher = users.findById(teacherId).orElseThrow(
                () -> new NotFoundException(NotFoundException.NotFoundExceptionType.USER_NOT_FOUND)
        );
        Classroom classroom = classrooms.findById(classroomId).orElseThrow(
                () -> new NotFoundException(NotFoundException.NotFoundExceptionType.CLASS_NOT_FOUND)
        );
        subject.setTeacher(teacher);
        subject.setClassroom(classroom);
        return subjects.create(subject);
    }

    @Override
    public Subject update(Subject subject) {
        findById(subject.getId());
        return subjects.update(subject);
    }

    @Override
    public Subject assignGroup(UUID subjectId, UUID groupId) {
        Subject subject = findById(subjectId);
        Group group = groups.findById(groupId).orElseThrow(
                () -> new NotFoundException(NotFoundException.NotFoundExceptionType.GROUP_NOT_FOUND)
        );
        if (subject.getGroups().contains(group)) {
            return subject;
        }
        group.setSubject(subject);
        subject.addGroup(group);
        subjects.update(subject);
        return subject;
    }

    @Override
    public Subject removeGroup(UUID subjectId, UUID groupId) {
        Subject subject = findById(subjectId);
        Group group = groups.findById(groupId).orElseThrow(
                () -> new NotFoundException(NotFoundException.NotFoundExceptionType.GROUP_NOT_FOUND)
        );
        if (!checkGroupInSubject(subject, groupId)) {
            return subject;
        }
        subject.removeGroup(group);
        group.setSubject(null);
        groups.update(group);
        return subject;
    }

    @Override
    public Subject assignGroups(UUID subjectId, List<UUID> groupIds) {
        Subject subject = findById(subjectId);
        for (UUID groupId : groupIds) {
            Group group = groups.findById(groupId).orElseThrow(
                    () -> new NotFoundException(NotFoundException.NotFoundExceptionType.GROUP_NOT_FOUND)
            );
            if (!subject.getGroups().contains(group)) {
                group.setSubject(subject);
                subject.addGroup(group);
            }
        }
        subjects.update(subject);
        return subject;
    }

    @Override
    public Subject findById(UUID id) {
        return subjects.findById(id).orElseThrow(() -> new NotFoundException(NotFoundException.NotFoundExceptionType.SUBJECT_NOT_FOUND));
    }

    @Override
    public void deleteById(UUID id) {
        findById(id);
        subjects.deleteById(id);
    }

    @Override
    public void deleteAll() {
        subjects.deleteAll();
    }

    @Override
    public List<Subject> findAll() {
        return subjects.findAll();
    }

    @Override
    public Page<Subject> findAllPaginated(String search, Pageable pageable) {
        return subjects.findAllPaginated(search, pageable);
    }

    private boolean checkGroupInSubject(Subject subject, UUID groupId) {
        return subject.getGroups().stream().anyMatch(group -> group.getId().equals(groupId));
    }
}