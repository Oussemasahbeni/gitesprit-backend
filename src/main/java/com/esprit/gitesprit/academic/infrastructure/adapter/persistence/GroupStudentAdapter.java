package com.esprit.gitesprit.academic.infrastructure.adapter.persistence;

import com.esprit.gitesprit.academic.domain.model.GroupStudent;
import com.esprit.gitesprit.academic.domain.port.output.GroupStudents;
import com.esprit.gitesprit.academic.infrastructure.entity.GroupStudentEntity;
import com.esprit.gitesprit.academic.infrastructure.mapper.GroupStudentMapper;
import com.esprit.gitesprit.academic.infrastructure.repository.GroupStudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Transactional
public class GroupStudentAdapter implements GroupStudents {

    private final GroupStudentRepository repository;
    private final GroupStudentMapper mapper;

    @Override
    public GroupStudent create(GroupStudent groupStudent) {
        GroupStudentEntity entity = mapper.toEntity(groupStudent);
        GroupStudentEntity saved = repository.save(entity);
        return mapper.toModel(saved);
    }

    @Override
    public GroupStudent update(GroupStudent groupStudent) {
        GroupStudentEntity entity = mapper.toEntity(groupStudent);
        GroupStudentEntity saved = repository.save(entity);
        return mapper.toModel(saved);
    }

    @Override
    public Optional<GroupStudent> findById(UUID id) {
        return repository.findById(id)
                .map(mapper::toModel);
    }

    @Override
    public List<GroupStudent> findByGroupId(UUID groupId) {
        return repository.findByGroupId(groupId).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<GroupStudent> findByStudentId(UUID studentId) {
        return repository.findByStudentId(studentId).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<GroupStudent> findByGroupIdAndStudentId(UUID groupId, UUID studentId) {
        return repository.findByGroupIdAndStudentId(groupId, studentId)
                .map(mapper::toModel);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}
