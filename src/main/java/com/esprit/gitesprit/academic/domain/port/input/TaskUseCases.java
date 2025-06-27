package com.esprit.gitesprit.academic.domain.port.input;

import com.esprit.gitesprit.academic.domain.model.Group;
import com.esprit.gitesprit.academic.domain.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface TaskUseCases {
    Task create(Task task, UUID groupStudentId);

    Task update(Task task, UUID groupStudentId);

    Task findById(UUID id);

    void deleteById(UUID id);

    void deleteAll();

    List<Task> findAllByGroupStudent(UUID id);

    Page<Task> findAllPaginated(String search, Pageable pageable);
}
