package com.esprit.gitesprit.academic.domain.port.output;

import com.esprit.gitesprit.academic.domain.model.Classroom;
import com.esprit.gitesprit.academic.domain.model.Subject;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface Classrooms {
    Classroom create(Classroom classroom);

    Classroom update(Classroom classroom);

    Classroom assignSubject(Classroom classroom, Subject subject);

    Classroom removeSubject(UUID classroomId, UUID subjectId);

    Optional<Classroom> findById(UUID id);

    void deleteById(UUID id);

    void deleteAll();

    List<Classroom> findAll();

    Page<Classroom> findAllPaginated(String search, Pageable pageable);
}
