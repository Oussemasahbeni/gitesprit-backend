package com.esprit.gitesprit.academic.domain.port.input;

import com.esprit.gitesprit.academic.domain.model.Subject;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;

public interface SubjectUseCases {
    Subject create(Subject subject);

    Subject update(Subject subject);

    Subject assignGroup(UUID subjectId, UUID groupId);

    Subject removeGroup(UUID subjectId, UUID groupId);

    Subject assignGroups(UUID subjectId, List<UUID> groupIds);

    Subject findById(UUID id);

    void deleteById(UUID id);

    void deleteAll();

    List<Subject> findAll();

    Page<Subject> findAllPaginated(String search, Pageable pageable);
}
