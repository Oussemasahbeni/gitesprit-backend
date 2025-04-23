package com.esprit.gitesprit.logs.infra.controller;

import com.esprit.gitesprit.logs.domain.enums.ActionType;
import com.esprit.gitesprit.logs.domain.enums.ModuleType;
import com.esprit.gitesprit.logs.domain.service.LogEntryService;
import com.esprit.gitesprit.logs.infra.dto.LogEntryDto;
import com.esprit.gitesprit.logs.infra.mapper.LogEntryMapper;
import com.esprit.gitesprit.shared.pagination.PaginationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/logs")
@Tag(name = "Log Entry API", description = "API for managing log entries")
public class LogEntryController {

  private final LogEntryService logEntryService;
  private final LogEntryMapper logEntryMapper;

  @GetMapping("/{id}")
  @Operation(summary = "Get Log Entry by ID", description = "Retrieve a log entry by its unique ID")
  public LogEntryDto getById(
      @Parameter(description = "ID of the log entry to retrieve", required = true) @PathVariable
          UUID id) {
    return logEntryMapper.toDto(logEntryService.getById(id));
  }

  @GetMapping("/module-types")
  @Operation(
      summary = "Get All Module Types",
      description = "Retrieve all available module types for log entries")
  public List<ModuleType> getAllModuleTypes() {
    return List.of(ModuleType.values());
  }

  @GetMapping("/action-types")
  @Operation(
      summary = "Get All Action Types",
      description = "Retrieve all available action types for log entries")
  public List<ActionType> getAllActionTypes() {
    return List.of(ActionType.values());
  }

  @GetMapping
  @Operation(summary = "Get All Log Entries", description = "Retrieve all log entries")
  public List<LogEntryDto> getAllLogEntries() {
    return logEntryService.getAllLogEntries().stream()
        .map(logEntryMapper::toDto)
        .collect(Collectors.toList());
  }

  @GetMapping("/page")
  @Operation(
      summary = "Get Paginated Log Entries",
      description = "Retrieve log entries with pagination")
  public Page<LogEntryDto> getPage(
      @Parameter(description = "Page size", example = "10") @RequestParam(defaultValue = "10")
          int size,
      @Parameter(description = "Search term") @RequestParam(defaultValue = "") String search,
      @Parameter(description = "Page number (starting from 0)", example = "0")
          @RequestParam(defaultValue = "0")
          int page,
      @Parameter(description = "Sort field", example = "id")
          @RequestParam(defaultValue = "id", required = false)
          String sort,
      @Parameter(description = "Sort direction (ASC or DESC)", example = "ASC")
          @RequestParam(defaultValue = "ASC")
          String sortDirection,
      @Parameter(description = "Module type", example = "USER") @RequestParam(required = false)
          ModuleType moduleType,
      @Parameter(description = "Action type", example = "CREATE") @RequestParam(required = false)
          ActionType actionType) {

    Pageable pageable = PaginationUtils.createPageable(page, size, sort, sortDirection);

    return logEntryService
        .getPage(actionType, moduleType, search, pageable)
        .map(logEntryMapper::toDto);
  }

  @DeleteMapping("/{id}")
  @Operation(
      summary = "Delete Log Entry by ID",
      description = "Delete a log entry by its unique ID")
  public void deleteById(
      @Parameter(description = "ID of the log entry to delete", required = true) @PathVariable
          UUID id) {
    logEntryService.deleteById(id);
  }

  @DeleteMapping("/batch")
  @Operation(
      summary = "Delete Log Entries in Batch",
      description = "Delete multiple log entries by their unique IDs")
  public void deleteByIds(
      @Parameter(description = "IDs of the log entries to delete", required = true) @RequestBody
          List<UUID> ids) {
    logEntryService.deleteByIds(ids);
  }
}
