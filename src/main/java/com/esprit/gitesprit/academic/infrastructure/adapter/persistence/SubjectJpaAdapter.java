package com.esprit.gitesprit.academic.infrastructure.adapter.persistence;

import com.esprit.gitesprit.academic.domain.model.Subject;
import com.esprit.gitesprit.academic.domain.port.output.Subjects;
import com.esprit.gitesprit.academic.infrastructure.entity.SubjectEntity;
import com.esprit.gitesprit.academic.infrastructure.mapper.SubjectMapper;
import com.esprit.gitesprit.academic.infrastructure.repository.SubjectRepository;
import com.esprit.gitesprit.exception.NotFoundException;
import com.esprit.gitesprit.shared.annotation.PersistenceAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@PersistenceAdapter
@RequiredArgsConstructor
@Transactional
public class SubjectJpaAdapter implements Subjects {
    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;

    @Override
    public Subject create(Subject subject) {
        SubjectEntity subjectEntity = subjectMapper.toEntity(subject);
        SubjectEntity savedAcademicEntity = subjectRepository.save(subjectEntity);
        return subjectMapper.toModel(savedAcademicEntity);
    }

    @Override
    public Subject update(Subject subject) {
        SubjectEntity oldSubjectEntity =
                subjectRepository
                        .findById(subject.getId())
                        .orElseThrow(
                                () ->
                                        new NotFoundException(
                                                NotFoundException.NotFoundExceptionType.ACADEMIC_YEAR_NOT_FOUND));
        SubjectEntity subjectEntity = subjectMapper.toEntity(subject);
        SubjectEntity updatedSubjectEntity =
                subjectMapper.partialUpdate(subjectEntity, oldSubjectEntity);
        SubjectEntity savedSubjectEntity = subjectRepository.save(updatedSubjectEntity);
        return subjectMapper.toModel(savedSubjectEntity);
    }

    @Override
    public Optional<Subject> findById(UUID id) {
        return subjectRepository.findById(id).map(subjectMapper::toModel);
    }

    @Override
    public void deleteById(UUID id) {
        subjectRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        subjectRepository.deleteAll();
    }

    @Override
    public List<Subject> findAll() {
        return subjectRepository.findAll().stream().map(subjectMapper::toModel).toList();
    }

    @Override
    public Page<Subject> findAllPaginated(String search, Pageable pageable) {
        return null;
    }
}

