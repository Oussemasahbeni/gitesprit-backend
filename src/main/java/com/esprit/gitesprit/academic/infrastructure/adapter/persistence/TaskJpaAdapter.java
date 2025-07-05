package com.esprit.gitesprit.academic.infrastructure.adapter.persistence;

import com.esprit.gitesprit.academic.domain.model.Task;
import com.esprit.gitesprit.academic.domain.port.output.Tasks;
import com.esprit.gitesprit.academic.infrastructure.adapter.specification.TaskSpecification;
import com.esprit.gitesprit.academic.infrastructure.entity.TaskEntity;
import com.esprit.gitesprit.academic.infrastructure.entity.TaskEntity;
import com.esprit.gitesprit.academic.infrastructure.mapper.TaskMapper;
import com.esprit.gitesprit.academic.infrastructure.repository.TaskRepository;
import com.esprit.gitesprit.exception.NotFoundException;
import com.esprit.gitesprit.shared.annotation.PersistenceAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.esprit.gitesprit.academic.infrastructure.adapter.specification.TaskSpecification.*;

@PersistenceAdapter
@RequiredArgsConstructor
@Transactional
public class TaskJpaAdapter implements Tasks {
    
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    
    @Override
    public Task create(Task task) {
        TaskEntity taskEntity = taskMapper.toEntity(task);
        TaskEntity savedTaskEntity = taskRepository.save(taskEntity);
        return taskMapper.toModel(savedTaskEntity);
    }

    @Override
    public Task update(Task task) {
        TaskEntity oldTaskEntity =
                taskRepository
                        .findById(task.getId())
                        .orElseThrow(
                                () ->
                                        new NotFoundException(
                                                NotFoundException.NotFoundExceptionType.TASK_NOT_FOUND));
        TaskEntity taskEntity = taskMapper.toEntity(task);
        TaskEntity updatedTaskEntity =
                taskMapper.partialUpdate(taskEntity, oldTaskEntity);
        TaskEntity savedTaskEntity = taskRepository.save(updatedTaskEntity);
        return taskMapper.toModel(savedTaskEntity);
    }

    @Override
    public Optional<Task> findById(UUID id) {
        return taskRepository.findById(id).map(taskMapper::toModel);
    }

    @Override
    public void deleteById(UUID id) {
        taskRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        taskRepository.deleteAll();
    }

    @Override
    public List<Task> findAllByGroupStudent(UUID id) {
        return taskRepository.findAllByGroupStudentId(id).stream().map(taskMapper::toModel).toList();
    }

    @Override
    public Page<Task> findAllPaginated(String search, Pageable pageable) {
        Specification<TaskEntity> spec = hasCriteria(search);
        return taskRepository.findAll(spec, pageable).map(taskMapper::toModel);
    }
}
