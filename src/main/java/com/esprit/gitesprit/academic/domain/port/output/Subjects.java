package com.esprit.gitesprit.academic.domain.port.output;

import com.esprit.gitesprit.academic.domain.model.Subject;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface Subjects {
    Subject create(Subject subject);

    Subject update(Subject subject);

    Optional<Subject> findById(UUID id);

    void deleteById(UUID id);

    void deleteAll();

    List<Subject> findAll();

    Page<Subject> findAllPaginated(String search, Pageable pageable);
}
