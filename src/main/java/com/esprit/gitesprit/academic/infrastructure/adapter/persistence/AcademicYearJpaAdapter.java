package com.esprit.gitesprit.academic.infrastructure.adapter.persistence;

import com.esprit.gitesprit.academic.domain.model.AcademicYear;
import com.esprit.gitesprit.academic.domain.port.output.AcademicYears;
import com.esprit.gitesprit.academic.infrastructure.entity.AcademicYearEntity;
import com.esprit.gitesprit.academic.infrastructure.mapper.AcademicYearMapper;
import com.esprit.gitesprit.academic.infrastructure.repository.AcademicYearRepository;
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
public class AcademicYearJpaAdapter implements AcademicYears {
    private final AcademicYearRepository academicYearRepository;
    private final AcademicYearMapper academicYearMapper;

    @Override
    public AcademicYear create(AcademicYear academicYear) {
        AcademicYearEntity academicYearEntity = academicYearMapper.toEntity(academicYear);
        AcademicYearEntity savedAcademicEntity = academicYearRepository.save(academicYearEntity);
        return academicYearMapper.toModel(savedAcademicEntity);
    }

    @Override
    public AcademicYear update(AcademicYear academicYear) {
        AcademicYearEntity oldAcademicYearEntity =
            academicYearRepository
                .findById(academicYear.getId())
                .orElseThrow(
                    () ->
                        new NotFoundException(
                            NotFoundException.NotFoundExceptionType.ACADEMIC_YEAR_NOT_FOUND));
        AcademicYearEntity academicYearEntity = academicYearMapper.toEntity(academicYear);
        AcademicYearEntity updatedAcademicYearEntity =
                academicYearMapper.partialUpdate(academicYearEntity, oldAcademicYearEntity);
        AcademicYearEntity savedAcademicYearEntity = academicYearRepository.save(updatedAcademicYearEntity);
        return academicYearMapper.toModel(savedAcademicYearEntity);
    }

    @Override
    public Optional<AcademicYear> findById(UUID id) {
        return academicYearRepository.findById(id).map(academicYearMapper::toModel);
    }

    @Override
    public void deleteById(UUID id) {
        academicYearRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        academicYearRepository.deleteAll();
    }

    @Override
    public List<AcademicYear> findAll() {
        return academicYearRepository.findAll().stream().map(academicYearMapper::toModel).toList();
    }

    @Override
    public Page<AcademicYear> findAllPaginated(String search, Pageable pageable) {
        return null;
    }
}
