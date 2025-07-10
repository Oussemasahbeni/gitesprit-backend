package com.esprit.gitesprit.academic.domain.service;

import com.esprit.gitesprit.academic.domain.model.GroupStudent;
import com.esprit.gitesprit.academic.domain.model.Task;
import com.esprit.gitesprit.academic.domain.port.input.TaskUseCases;
import com.esprit.gitesprit.academic.domain.port.output.GroupStudents;
import com.esprit.gitesprit.academic.domain.port.output.Tasks;
import com.esprit.gitesprit.exception.NotFoundException;
import com.esprit.gitesprit.shared.annotation.DomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@DomainService
@RequiredArgsConstructor
public class TaskService implements TaskUseCases {

    private final Tasks tasks;
    private final GroupStudents groupStudents;

    @Override
    public Task create(Task task, UUID groupStudentId) {
        GroupStudent groupStudent = groupStudents.findById(groupStudentId).orElseThrow(
                () ->
                        new NotFoundException(
                                NotFoundException.NotFoundExceptionType.GROUP_NOT_FOUND)
        );
        task.setGroupStudent(groupStudent);
        return tasks.create(task);
    }

    @Override
    public Task update(Task task, UUID groupStudentId) {
        findById(task.getId());
        return tasks.update(task);
    }

    @Override
    public Task assignBranch(UUID id, List<String> links) {
        Task task = findById(id);
        task.setBranchLinks(links);
        return tasks.update(task);
    }

    @Override
    public Task markAsDone(UUID id) {
        Task task = findById(id);
        task.setDone(true);
        return tasks.update(task);
    }

    @Override
    public Task findById(UUID id) {
        return tasks.findById(id).orElseThrow(
                () ->
                        new NotFoundException(
                                NotFoundException.NotFoundExceptionType.TASK_NOT_FOUND)
        );
    }

    @Override
    public void deleteById(UUID id) {
        findById(id);
        tasks.deleteById(id);
    }

    @Override
    public void deleteAll() {
        deleteAll();
    }

    @Override
    public List<Task> findAllByGroupStudent(UUID id) {
        return tasks.findAllByGroupStudent(id);
    }

    @Override
    public Page<Task> findAllPaginated(String search, Pageable pageable) {
        return tasks.findAllPaginated(search, pageable);
    }
}
