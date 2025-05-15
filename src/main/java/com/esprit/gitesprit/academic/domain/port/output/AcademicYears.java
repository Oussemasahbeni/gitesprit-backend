package com.esprit.gitesprit.academic.domain.port.output;

import com.esprit.gitesprit.academic.domain.model.AcademicYear;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AcademicYears {
    AcademicYear create(AcademicYear academicYear);

    AcademicYear update(AcademicYear academicYear);

    Optional<AcademicYear> findById(UUID id);

    void deleteById(UUID id);

    void deleteAll();

    List<AcademicYear> findAll();

    Page<AcademicYear> findAllPaginated(Pageable pageable);
}
