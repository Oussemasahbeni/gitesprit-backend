package com.esprit.gitesprit.academic.domain.port.output;

import com.esprit.gitesprit.academic.domain.model.Group;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface Groups {
    Group create(Group group);

    Group update(Group group);

    Group assignStudent(Group group, UUID student);

    Group removeStudent(UUID groupId, UUID student);

    Group assignStudents(Group group, List<UUID> students);

    Optional<Group> findById(UUID id);

    void deleteById(UUID id);

    void deleteAll();

    List<Group> findAll();

    Page<Group> findAllPaginated(String search, Pageable pageable);
}
