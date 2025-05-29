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

    Optional<Group> findById(UUID id);

    void deleteById(UUID id);

    void deleteAll();

    List<Group> findAll();

    List<Group> findAllByStudentId(UUID studentId);

    Page<Group> findAllPaginated(String search, Pageable pageable);
}
