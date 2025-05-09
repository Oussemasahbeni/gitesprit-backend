package com.esprit.gitesprit.academic.domain.port.input;

import com.esprit.gitesprit.academic.domain.model.Classroom;
import com.esprit.gitesprit.academic.domain.model.Classroom;
import com.esprit.gitesprit.academic.domain.model.Subject;
import com.esprit.gitesprit.users.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ClassroomUseCases {
    Classroom create(Classroom classroom, UUID academicYearId);

    Classroom update(Classroom classroom);

    Classroom assignSubject(UUID classroomId, Subject subject);

    Classroom removeSubject(UUID classroomId, UUID subjectId);

    Classroom findById(UUID id);

    void deleteById(UUID id);

    void deleteAll();

    List<Classroom> findAll();

    Page<Classroom> findAllPaginated(String search, Pageable pageable);
}
