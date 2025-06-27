package com.esprit.gitesprit.academic.infrastructure.controller;

import com.esprit.gitesprit.academic.domain.model.Task;
import com.esprit.gitesprit.academic.domain.port.input.TaskUseCases;
import com.esprit.gitesprit.academic.infrastructure.dto.request.AddTaskDto;
import com.esprit.gitesprit.academic.infrastructure.dto.response.TaskDto;
import com.esprit.gitesprit.academic.infrastructure.mapper.TaskMapper;
import com.esprit.gitesprit.shared.pagination.CustomPage;
import com.esprit.gitesprit.shared.pagination.PageMapper;
import com.esprit.gitesprit.shared.pagination.PaginationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tasks")
@Tag(name = "Tasks", description = "API for managing tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskUseCases taskUseCases;
    private final TaskMapper taskMapper;

    @PostMapping
    public ResponseEntity<TaskDto> create(@RequestBody @Valid AddTaskDto dto){
        Task task = taskMapper.toModelFromDto(dto);
        Task savedTask = taskUseCases.create(task, dto.groupStudentId());
        return ResponseEntity.ok(taskMapper.toResponseDto(savedTask));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> findById(@PathVariable UUID id){
        Task task = taskUseCases.findById(id);
        TaskDto taskSimpleDto = taskMapper.toResponseDto(task);
        return ResponseEntity.ok(taskSimpleDto);
    }

    @GetMapping
    @Operation(
            summary = "Get all Tasks paginated",
            description = "Retrieves Tasks with pagination, search, and sorting.")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful retrieval",
                            content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CustomPage.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid pagination parameters", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            })
    public ResponseEntity<CustomPage<TaskDto>> findAllPaginated(
            @Parameter(description = "Page size", example = "10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Search term") @RequestParam(defaultValue = "") String search,
            @Parameter(description = "Page number (starting from 0)", example = "0") @RequestParam(defaultValue = "0")
            int page,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id", required = false)
            String sort,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "ASC")
            @RequestParam(defaultValue = "ASC")
            String sortDirection) {
        Pageable pageable = PaginationUtils.createPageable(page, size, sort, sortDirection);
        Page<TaskDto> tasks =
                taskUseCases.findAllPaginated(search, pageable).map(taskMapper::toResponseDto);
        return ResponseEntity.ok(PageMapper.toCustomPage(tasks));
    }

    @GetMapping("/group-student/{groupStudentId}")
    @Operation(summary = "Get all Tasks of a group student", description = "Retrieves a list of all Tasks of a group student without pagination.")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful retrieval",
                            content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = TaskDto.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            })
    public ResponseEntity<List<TaskDto>> findAllByGroupStudentId(@PathVariable UUID groupStudentId) {
        List<TaskDto> tasks = taskUseCases.findAllByGroupStudent(groupStudentId).stream().map(taskMapper::toResponseDto).toList();
        return ResponseEntity.ok(tasks);
    }
}
