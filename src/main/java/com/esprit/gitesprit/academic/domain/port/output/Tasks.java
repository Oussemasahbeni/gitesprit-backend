package com.esprit.gitesprit.academic.domain.port.output;

import com.esprit.gitesprit.academic.domain.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Tasks {
    Task create(Task task);

    Task update(Task task);

    Optional<Task> findById(UUID id);

    void deleteById(UUID id);

    void deleteAll();

    List<Task> findAllByGroupStudent(UUID id);

    Page<Task> findAllPaginated(String search, Pageable pageable);
}
