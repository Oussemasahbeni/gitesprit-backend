package com.esprit.gitesprit.academic.infrastructure.mapper;

import com.esprit.gitesprit.academic.domain.model.Subject;
import com.esprit.gitesprit.academic.infrastructure.dto.request.AddSubjectDto;
import com.esprit.gitesprit.academic.infrastructure.dto.response.SubjectDto;
import com.esprit.gitesprit.academic.infrastructure.dto.response.SubjectSimpleDto;
import com.esprit.gitesprit.academic.infrastructure.entity.SubjectEntity;
import com.esprit.gitesprit.shared.mapstruct.CycleAvoidingMappingContext;
import com.esprit.gitesprit.shared.mapstruct.DoIgnore;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface SubjectMapper {

  SubjectEntity mapToEntity(Subject subject, @Context CycleAvoidingMappingContext context);

  Subject mapToModel(SubjectEntity subjectEntity, @Context CycleAvoidingMappingContext context);

  @DoIgnore
  default SubjectEntity toEntity(Subject subject) {
    return mapToEntity(subject, new CycleAvoidingMappingContext());
  }

  @DoIgnore
  default Subject toModel(SubjectEntity subjectEntity) {
    return mapToModel(subjectEntity, new CycleAvoidingMappingContext());
  }

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  SubjectEntity partialUpdate(SubjectEntity subject, @MappingTarget SubjectEntity target);

  Subject toModelFromDto(AddSubjectDto subjectDto);

  // Subject toModelFromUpdateDto( subjectDto);

  SubjectSimpleDto toSimpleDto(Subject subject);

  SubjectDto toResponseDto(Subject subject);
}
