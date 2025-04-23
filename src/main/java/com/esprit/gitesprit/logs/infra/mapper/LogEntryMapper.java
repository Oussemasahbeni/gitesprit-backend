package com.esprit.gitesprit.logs.infra.mapper;

import com.esprit.gitesprit.logs.domain.model.LogEntry;
import com.esprit.gitesprit.logs.infra.dto.LogEntryDto;
import com.esprit.gitesprit.logs.infra.entity.LogEntryEntity;
import org.mapstruct.Mapper;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = IGNORE)
public interface LogEntryMapper {

  LogEntry toModel(LogEntryEntity logEntryEntity);

  LogEntryEntity toEntity(LogEntry logEntry);

  LogEntryDto toDto(LogEntry logEntry);
}
