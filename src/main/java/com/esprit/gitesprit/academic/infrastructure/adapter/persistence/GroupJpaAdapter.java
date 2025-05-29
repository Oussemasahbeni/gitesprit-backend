package com.esprit.gitesprit.academic.infrastructure.adapter.persistence;

import com.esprit.gitesprit.academic.domain.model.Group;
import com.esprit.gitesprit.academic.domain.port.output.Groups;
import com.esprit.gitesprit.academic.infrastructure.adapter.specification.GroupSpecification;
import com.esprit.gitesprit.academic.infrastructure.entity.GroupEntity;
import com.esprit.gitesprit.academic.infrastructure.mapper.GroupMapper;
import com.esprit.gitesprit.academic.infrastructure.repository.GroupRepository;
import com.esprit.gitesprit.exception.NotFoundException;
import com.esprit.gitesprit.shared.annotation.PersistenceAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@PersistenceAdapter
@RequiredArgsConstructor
@Transactional
public class GroupJpaAdapter implements Groups {
    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;

    @Override
    public Group create(Group group) {
        GroupEntity groupEntity = groupMapper.toEntity(group);
        GroupEntity savedAcademicEntity = groupRepository.save(groupEntity);
        System.out.println(savedAcademicEntity +"hello");
        return groupMapper.toModel(savedAcademicEntity);
    }

    @Override
    public Group update(Group group) {
        GroupEntity oldGroupEntity =
                groupRepository
                        .findById(group.getId())
                        .orElseThrow(
                                () ->
                                        new NotFoundException(
                                                NotFoundException.NotFoundExceptionType.ACADEMIC_YEAR_NOT_FOUND));
        GroupEntity groupEntity = groupMapper.toEntity(group);
        GroupEntity updatedGroupEntity =
                groupMapper.partialUpdate(groupEntity, oldGroupEntity);
        GroupEntity savedGroupEntity = groupRepository.save(updatedGroupEntity);
        return groupMapper.toModel(savedGroupEntity);
    }

    @Override
    public Optional<Group> findById(UUID id) {
        return groupRepository.findById(id).map(groupMapper::toModel);
    }

    @Override
    public void deleteById(UUID id) {
        groupRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        groupRepository.deleteAll();
    }

    @Override
    public List<Group> findAll() {
        return groupRepository.findAll().stream().map(groupMapper::toModel).toList();
    }

    @Override
    public Page<Group> findAllPaginated(String search, Pageable pageable) {
        Specification<GroupEntity> spec = GroupSpecification.hasCriteria(search);
        return groupRepository.findAll(spec, pageable).map(groupMapper::toModel);
    }
}

