package com.esprit.gitesprit.academic.infrastructure.mapper;

import com.esprit.gitesprit.academic.domain.model.Classroom;
import com.esprit.gitesprit.academic.infrastructure.dto.request.AddClassroomDto;
import com.esprit.gitesprit.academic.infrastructure.dto.response.ClassroomDto;
import com.esprit.gitesprit.academic.infrastructure.entity.ClassroomEntity;
import com.esprit.gitesprit.shared.mapstruct.CycleAvoidingMappingContext;
import com.esprit.gitesprit.shared.mapstruct.DoIgnore;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ClassroomMapper {
  ClassroomEntity mapToEntity(Classroom classroom, @Context CycleAvoidingMappingContext context);

  Classroom mapToModel(
      ClassroomEntity classroomEntity, @Context CycleAvoidingMappingContext context);

  @DoIgnore
  default ClassroomEntity toEntity(Classroom classroom) {
    return mapToEntity(classroom, new CycleAvoidingMappingContext());
  }

  @DoIgnore
  default Classroom toModel(ClassroomEntity classroomEntity) {
    return mapToModel(classroomEntity, new CycleAvoidingMappingContext());
  }

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  ClassroomEntity partialUpdate(ClassroomEntity classroom, @MappingTarget ClassroomEntity target);

  Classroom toModelFromDto(AddClassroomDto classroomDto);

  // Classroom toModelFromUpdateDto( classroomDto);

  ClassroomDto toResponseDto(Classroom classroom);
}
