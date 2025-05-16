package com.esprit.gitesprit.academic.infrastructure.controller;

import com.esprit.gitesprit.academic.domain.model.Classroom;
import com.esprit.gitesprit.academic.domain.port.input.ClassroomUseCases;
import com.esprit.gitesprit.academic.infrastructure.dto.request.AddClassroomDto;
import com.esprit.gitesprit.academic.infrastructure.dto.response.ClassroomDto;
import com.esprit.gitesprit.academic.infrastructure.dto.response.ClassroomSimpleDto;
import com.esprit.gitesprit.academic.infrastructure.mapper.ClassroomMapper;
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
@RequestMapping("/api/v1/classrooms")
@Tag(name = "Classrooms", description = "API for managing classrooms")
@RequiredArgsConstructor
public class ClassroomController {
    private final ClassroomUseCases classroomUseCases;
    private final ClassroomMapper classroomMapper;

    @PostMapping
    public ResponseEntity<ClassroomSimpleDto> create(@RequestBody @Valid AddClassroomDto dto){
        Classroom classroom = classroomMapper.toModelFromDto(dto);
        Classroom savedClassroom = classroomUseCases.create(classroom, dto.academicYearId());
        return ResponseEntity.ok(classroomMapper.toSimpleDto(savedClassroom));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassroomDto> findById(@PathVariable UUID id){
        Classroom classroom = classroomUseCases.findById(id);
        ClassroomDto classroomSimpleDto = classroomMapper.toResponseDto(classroom);
        return ResponseEntity.ok(classroomSimpleDto);
    }

    @GetMapping
    @Operation(
            summary = "Get all Classrooms paginated",
            description = "Retrieves Classrooms with pagination, search, and sorting.")
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
    public ResponseEntity<CustomPage<ClassroomDto>> findAllPaginated(
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
        Page<ClassroomDto> classrooms =
                classroomUseCases.findAllPaginated(search, pageable).map(classroomMapper::toResponseDto);
        return ResponseEntity.ok(PageMapper.toCustomPage(classrooms));
    }

    @GetMapping("/all")
    @Operation(summary = "Get all Classrooms", description = "Retrieves a list of all Classrooms without pagination.")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful retrieval",
                            content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ClassroomSimpleDto.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            })
    public ResponseEntity<List<ClassroomDto>> findAll() {
        List<ClassroomDto> classrooms = classroomUseCases.findAll().stream().map(classroomMapper::toResponseDto).toList();
        return ResponseEntity.ok(classrooms);
    }
}
