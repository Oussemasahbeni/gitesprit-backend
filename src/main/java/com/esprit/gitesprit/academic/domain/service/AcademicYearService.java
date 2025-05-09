package com.esprit.gitesprit.academic.domain.service;

import com.esprit.gitesprit.academic.domain.model.AcademicYear;
import com.esprit.gitesprit.academic.domain.model.Classroom;
import com.esprit.gitesprit.academic.domain.port.input.AcademicYearUseCases;
import com.esprit.gitesprit.academic.domain.port.input.ClassroomUseCases;
import com.esprit.gitesprit.academic.domain.port.output.AcademicYears;
import com.esprit.gitesprit.academic.domain.port.output.Classrooms;
import com.esprit.gitesprit.exception.NotFoundException;
import com.esprit.gitesprit.shared.annotation.DomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@DomainService
@RequiredArgsConstructor
public class AcademicYearService implements AcademicYearUseCases {

    private final AcademicYears academicYears;
    private final ClassroomUseCases classroomUseCases;
    private final Classrooms classrooms;

    @Override
    public AcademicYear create(AcademicYear academicYear) {
        return academicYears.create(academicYear);
    }

    @Override
    public AcademicYear update(AcademicYear academicYear) {
        findById(academicYear.getId());
        return academicYears.update(academicYear);
    }

    @Override
    public AcademicYear assignClass(UUID academicYearId, Classroom classroom) {
        AcademicYear academicYear = findById(academicYearId);
        if (academicYear.getClassrooms().contains(classroom)) {
            return academicYear;
        }
        classroom.setAcademicYear(academicYear);
        academicYear.addClass(classroom);
        academicYears.update(academicYear);
        return academicYear;
    }

    @Override
    public AcademicYear removeClass(UUID academicYearId, UUID classroomId) {
        AcademicYear academicYear = findById(academicYearId);
        Classroom classroom = classroomUseCases.findById(classroomId);
        if (!checkClassInAcademicYear(academicYear, classroomId)) {
            return academicYear;
        }
        academicYear.removeClass(classroom);
        classroom.setAcademicYear(null);
        classrooms.update(classroom);
        return academicYear;
    }

    @Override
    public AcademicYear findById(UUID id) {
    return academicYears.findById(id).orElseThrow(() -> new NotFoundException(NotFoundException.NotFoundExceptionType.ACADEMIC_YEAR_NOT_FOUND));
    }

    @Override
    public void deleteById(UUID id) {
        findById(id);
        academicYears.deleteById(id);
    }

    @Override
    public void deleteAll() {
        academicYears.deleteAll();
    }

    @Override
    public List<AcademicYear> findAll() {
        return academicYears.findAll();
    }

    @Override
    public Page<AcademicYear> findAllPaginated(String search, Pageable pageable) {
        return academicYears.findAllPaginated(search, pageable);
    }

    private boolean checkClassInAcademicYear(AcademicYear academicYear, UUID classroomId) {
        return academicYear.getClassrooms().stream().anyMatch(classroom -> classroom.getId().equals(classroomId));
    }
}
