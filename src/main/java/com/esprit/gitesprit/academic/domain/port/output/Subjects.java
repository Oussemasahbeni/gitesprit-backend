package com.esprit.gitesprit.academic.domain.port.output;

import com.esprit.gitesprit.academic.domain.model.Subject;
import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;

public interface Subjects {
    Subject create(Subject subject);

    Subject update(Subject subject);

    Subject assignGroup(Subject subject, UUID groupId);

    Subject removeGroup(UUID subjectId, UUID groupId);

    Subject assignGroups(Subject subject, List<UUID> groupIds);

    Optional<Subject> findById(UUID id);

    void deleteById(UUID id);

    void deleteAll();

    List<Subject> findAll();

    Page<Subject> findAllPaginated(String search, Pageable pageable);
}
