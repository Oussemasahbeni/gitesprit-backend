package com.esprit.gitesprit.academic.domain.service;

import com.esprit.gitesprit.academic.domain.model.Group;
import com.esprit.gitesprit.academic.domain.model.Subject;
import com.esprit.gitesprit.academic.domain.port.input.ClassroomUseCases;
import com.esprit.gitesprit.academic.domain.port.input.GroupUseCases;
import com.esprit.gitesprit.academic.domain.port.input.SubjectUseCases;
import com.esprit.gitesprit.academic.domain.port.output.Classrooms;
import com.esprit.gitesprit.academic.domain.port.output.Groups;
import com.esprit.gitesprit.academic.domain.port.output.Subjects;
import com.esprit.gitesprit.exception.NotFoundException;
import com.esprit.gitesprit.shared.annotation.DomainService;
import com.esprit.gitesprit.users.domain.port.input.UserUseCases;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;

@DomainService
@RequiredArgsConstructor
public class SubjectService implements SubjectUseCases {
    private final Subjects subjects;
    private final UserUseCases userUseCases;
    private final GroupUseCases groupUseCases;
    private final Groups groups;
    private final ClassroomUseCases classroomUseCases;
    private final Classrooms classrooms;

    @Override
    public Subject create(Subject subject) {
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
        Group group = groupUseCases.findById(groupId);
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
        Group group = groupUseCases.findById(groupId);
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
            Group group = groupUseCases.findById(groupId);
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