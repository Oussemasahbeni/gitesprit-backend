package com.esprit.gitesprit.academic.domain.port.input;

import com.esprit.gitesprit.academic.domain.model.AcademicYear;
import com.esprit.gitesprit.academic.domain.model.Classroom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface AcademicYearUseCases {
    AcademicYear create(AcademicYear academicYear);

    AcademicYear update(AcademicYear academicYear);

    AcademicYear assignClass(UUID academicYearId, Classroom classroom);

    AcademicYear removeClass(UUID academicYearId, UUID classroomId);

    AcademicYear findById(UUID id);

    void deleteById(UUID id);

    void deleteAll();

    List<AcademicYear> findAll();

    Page<AcademicYear> findAllPaginated(Pageable pageable);
}
