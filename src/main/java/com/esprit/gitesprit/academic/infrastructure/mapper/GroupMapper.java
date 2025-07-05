package com.esprit.gitesprit.academic.infrastructure.mapper;

import com.esprit.gitesprit.academic.domain.model.Group;
import com.esprit.gitesprit.academic.infrastructure.dto.request.AddGroupDto;
import com.esprit.gitesprit.academic.infrastructure.dto.response.GroupDto;
import com.esprit.gitesprit.academic.infrastructure.dto.response.GroupSimpleDto;
import com.esprit.gitesprit.academic.infrastructure.entity.GroupEntity;
import com.esprit.gitesprit.shared.mapstruct.CycleAvoidingMappingContext;
import com.esprit.gitesprit.shared.mapstruct.DoIgnore;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface GroupMapper {
  GroupEntity mapToEntity(Group group, @Context CycleAvoidingMappingContext context);

  Group mapToModel(GroupEntity groupEntity, @Context CycleAvoidingMappingContext context);

  @DoIgnore
  default GroupEntity toEntity(Group group) {
    return mapToEntity(group, new CycleAvoidingMappingContext());
  }

  @DoIgnore
  default Group toModel(GroupEntity groupEntity) {
    return mapToModel(groupEntity, new CycleAvoidingMappingContext());
  }

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  GroupEntity partialUpdate(GroupEntity group, @MappingTarget GroupEntity target);

  Group toModelFromDto(AddGroupDto groupDto);

  // Group toModelFromUpdateDto( groupDto);

  @Mapping(target = "subjectName", source = "subject.name")
  GroupSimpleDto toSimpleDto(Group group);

  @Mapping(target = "nbRepositories", expression = "java(group.getRepositories() != null ? group.getRepositories().size() : 0)")
  GroupDto toResponseDto(Group group);
}
