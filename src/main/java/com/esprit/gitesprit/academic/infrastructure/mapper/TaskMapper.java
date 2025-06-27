package com.esprit.gitesprit.academic.infrastructure.mapper;

import com.esprit.gitesprit.academic.domain.model.Task;
import com.esprit.gitesprit.academic.infrastructure.dto.request.AddTaskDto;
import com.esprit.gitesprit.academic.infrastructure.dto.response.TaskDto;
import com.esprit.gitesprit.academic.infrastructure.entity.TaskEntity;
import com.esprit.gitesprit.shared.mapstruct.CycleAvoidingMappingContext;
import com.esprit.gitesprit.shared.mapstruct.DoIgnore;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskEntity mapToEntity(Task task, @Context CycleAvoidingMappingContext context);

    Task mapToModel(TaskEntity taskEntity, @Context CycleAvoidingMappingContext context);

    @DoIgnore
    default TaskEntity toEntity(Task task) {
        return mapToEntity(task, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default Task toModel(TaskEntity taskEntity) {
        return mapToModel(taskEntity, new CycleAvoidingMappingContext());
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    TaskEntity partialUpdate(TaskEntity task, @MappingTarget TaskEntity target);

    Task toModelFromDto(AddTaskDto taskDto);

    // Task toModelFromUpdateDto( taskDto);

//    TaskSimpleDto toSimpleDto(Task task);

    TaskDto toResponseDto(Task task);
}
