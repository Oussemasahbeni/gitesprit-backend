package com.esprit.gitesprit.academic.infrastructure.mapper;

import com.esprit.gitesprit.academic.domain.model.AcademicYear;
import com.esprit.gitesprit.academic.infrastructure.dto.request.AddAcademicYearDto;
import com.esprit.gitesprit.academic.infrastructure.dto.response.AcademicYearDto;
import com.esprit.gitesprit.academic.infrastructure.entity.AcademicYearEntity;
import com.esprit.gitesprit.shared.mapstruct.CycleAvoidingMappingContext;
import com.esprit.gitesprit.shared.mapstruct.DoIgnore;
import org.mapstruct.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AcademicYearMapper {
    AcademicYearEntity mapToEntity(AcademicYear academicYear, @Context CycleAvoidingMappingContext context);

    AcademicYear mapToModel(AcademicYearEntity academicYearEntity, @Context CycleAvoidingMappingContext context);

    @DoIgnore
    default AcademicYearEntity toEntity(AcademicYear academicYear) {
        return mapToEntity(academicYear, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default AcademicYear toModel(AcademicYearEntity academicYearEntity) {
        return mapToModel(academicYearEntity, new CycleAvoidingMappingContext());
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    AcademicYearEntity partialUpdate(AcademicYearEntity academicYear, @MappingTarget AcademicYearEntity target);

    AcademicYear toModelFromDto(AddAcademicYearDto academicYearDto);

    //AcademicYear toModelFromUpdateDto( academicYearDto);

    AcademicYearDto toResponseDto(AcademicYear academicYear);
}
