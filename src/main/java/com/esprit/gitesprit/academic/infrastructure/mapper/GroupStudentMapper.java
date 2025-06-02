package com.esprit.gitesprit.academic.infrastructure.mapper;

import com.esprit.gitesprit.academic.domain.model.GroupStudent;
import com.esprit.gitesprit.academic.infrastructure.dto.response.GroupStudentDto;
import com.esprit.gitesprit.academic.infrastructure.entity.GroupStudentEntity;
import com.esprit.gitesprit.shared.mapstruct.CycleAvoidingMappingContext;
import com.esprit.gitesprit.shared.mapstruct.DoIgnore;
import com.esprit.gitesprit.users.infrastructure.mapper.UserMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        uses = {UserMapper.class, GroupMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface GroupStudentMapper {


    GroupStudentEntity mapToEntity(GroupStudent group, @Context CycleAvoidingMappingContext context);

    GroupStudent mapToModel(GroupStudentEntity groupEntity, @Context CycleAvoidingMappingContext context);

    @DoIgnore
    default GroupStudentEntity toEntity(GroupStudent group) {
        return mapToEntity(group, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default GroupStudent toModel(GroupStudentEntity groupEntity) {
        return mapToModel(groupEntity, new CycleAvoidingMappingContext());
    }


    GroupStudentDto toDto(GroupStudent model);
}
