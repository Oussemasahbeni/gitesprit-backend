package com.esprit.gitesprit.academic.infrastructure.controller;

import com.esprit.gitesprit.academic.domain.model.Subject;
import com.esprit.gitesprit.academic.domain.port.input.SubjectUseCases;
import com.esprit.gitesprit.academic.infrastructure.dto.request.AddSubjectDto;
import com.esprit.gitesprit.academic.infrastructure.dto.response.SubjectEntityDto;
import com.esprit.gitesprit.academic.infrastructure.mapper.SubjectMapper;
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
@RequestMapping("/api/v1/subjects")
@Tag(name = "Subjects", description = "API for managing subjects")
@RequiredArgsConstructor
public class SubjectController {
    private final SubjectUseCases subjectUseCases;
    private final SubjectMapper subjectMapper;

    @PostMapping
    public ResponseEntity<SubjectEntityDto> create(@RequestBody @Valid AddSubjectDto dto) {
        Subject subject = subjectMapper.toModelFromDto(dto);
        Subject savedSubject = subjectUseCases.create(subject, dto.teacherId(), dto.classroomId());
        return ResponseEntity.ok(subjectMapper.toResponseDto(savedSubject));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectEntityDto> findById(@PathVariable UUID id) {
        Subject subject = subjectUseCases.findById(id);
        SubjectEntityDto subjectDto = subjectMapper.toResponseDto(subject);
        return ResponseEntity.ok(subjectDto);
    }

    @GetMapping
    @Operation(
            summary = "Get all Subjects paginated",
            description = "Retrieves Subjects with pagination, search, and sorting.")
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
    public ResponseEntity<CustomPage<SubjectEntityDto>> findAllPaginated(
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
        Page<SubjectEntityDto> subjects =
                subjectUseCases.findAllPaginated(search, pageable).map(subjectMapper::toResponseDto);
        return ResponseEntity.ok(PageMapper.toCustomPage(subjects));
    }

    @GetMapping("/all")
    @Operation(summary = "Get all Subjects", description = "Retrieves a list of all Subjects without pagination.")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful retrieval",
                            content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SubjectEntityDto.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            })
    public ResponseEntity<List<SubjectEntityDto>> findAll() {
        List<SubjectEntityDto> subjects = subjectUseCases.findAll().stream().map(subjectMapper::toResponseDto).toList();
        return ResponseEntity.ok(subjects);
    }
}
