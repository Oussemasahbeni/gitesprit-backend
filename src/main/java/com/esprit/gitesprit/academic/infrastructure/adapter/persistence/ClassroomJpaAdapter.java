package com.esprit.gitesprit.academic.infrastructure.adapter.persistence;

import com.esprit.gitesprit.academic.domain.model.Classroom;
import com.esprit.gitesprit.academic.domain.port.output.Classrooms;
import com.esprit.gitesprit.academic.infrastructure.adapter.specification.ClassroomSpecification;
import com.esprit.gitesprit.academic.infrastructure.entity.ClassroomEntity;
import com.esprit.gitesprit.academic.infrastructure.entity.ClassroomEntity;
import com.esprit.gitesprit.academic.infrastructure.entity.ClassroomEntity;
import com.esprit.gitesprit.academic.infrastructure.mapper.ClassroomMapper;
import com.esprit.gitesprit.academic.infrastructure.repository.ClassroomRepository;
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

import static com.esprit.gitesprit.academic.infrastructure.adapter.specification.ClassroomSpecification.*;

@PersistenceAdapter
@RequiredArgsConstructor
@Transactional
public class ClassroomJpaAdapter implements Classrooms {
    private final ClassroomRepository classroomRepository;
    private final ClassroomMapper classroomMapper;
    
    @Override
    public Classroom create(Classroom classroom) {
        ClassroomEntity classroomEntity = classroomMapper.toEntity(classroom);
        ClassroomEntity savedAcademicEntity = classroomRepository.save(classroomEntity);
        return classroomMapper.toModel(savedAcademicEntity);
    }

    @Override
    public Classroom update(Classroom classroom) {
        ClassroomEntity oldClassroomEntity =
                classroomRepository
                        .findById(classroom.getId())
                        .orElseThrow(
                                () ->
                                        new NotFoundException(
                                                NotFoundException.NotFoundExceptionType.ACADEMIC_YEAR_NOT_FOUND));
        ClassroomEntity classroomEntity = classroomMapper.toEntity(classroom);
        ClassroomEntity updatedClassroomEntity =
                classroomMapper.partialUpdate(classroomEntity, oldClassroomEntity);
        ClassroomEntity savedClassroomEntity = classroomRepository.save(updatedClassroomEntity);
        return classroomMapper.toModel(savedClassroomEntity);
    }

    @Override
    public Optional<Classroom> findById(UUID id) {
        return classroomRepository.findById(id).map(classroomMapper::toModel);
    }

    @Override
    public void deleteById(UUID id) {
        classroomRepository.deleteById(id);

    }

    @Override
    public void deleteAll() {
        classroomRepository.deleteAll();

    }

    @Override
    public List<Classroom> findAll() {
        return classroomRepository.findAll().stream().map(classroomMapper::toModel).toList();
    }

    @Override
    public Page<Classroom> findAllPaginated(String search, Pageable pageable) {
        Specification<ClassroomEntity> spec = hasCriteria(search);
        return classroomRepository.findAll(spec, pageable).map(classroomMapper::toModel);
    }
}
