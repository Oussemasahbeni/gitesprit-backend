package com.esprit.gitesprit.academic.infrastructure.controller;

import com.esprit.gitesprit.academic.domain.model.AcademicYear;
import com.esprit.gitesprit.academic.domain.port.input.AcademicYearUseCases;
import com.esprit.gitesprit.academic.infrastructure.dto.request.AddAcademicYearDto;
import com.esprit.gitesprit.academic.infrastructure.dto.response.AcademicYearDto;
import com.esprit.gitesprit.academic.infrastructure.dto.response.AcademicYearSimpleDto;
import com.esprit.gitesprit.academic.infrastructure.mapper.AcademicYearMapper;
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
@RequestMapping("/api/v1/academic-years")
@Tag(name = "Academic Years", description = "API for managing academic years")
@RequiredArgsConstructor
public class AcademicYearController {

    private final AcademicYearUseCases academicYearUseCases;
    private final AcademicYearMapper academicYearMapper;

    @PostMapping
    @Operation(summary = "Create a new academic year", description = "Creates a new academic year.")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Academic year created successfully",
                            content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AcademicYear.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            })
    public ResponseEntity<AcademicYearSimpleDto> create(
            @Parameter(description = "Academic year details in JSON format") @RequestBody @Valid AddAcademicYearDto dto) {
        AcademicYear academicYear = academicYearMapper.toModelFromDto(dto);
        AcademicYear createdAcademicYear = academicYearUseCases.create(academicYear);
        return ResponseEntity.ok(academicYearMapper.toSimpleDto(createdAcademicYear));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an academic year", description = "Updates an existing academic year's details.")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Academic year updated successfully",
                            content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AcademicYear.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Academic year not found", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            })
    public ResponseEntity<AcademicYearSimpleDto> update(
            @Parameter(description = "ID of the academic year to be updated", required = true) @PathVariable UUID id,
            @Parameter(description = "Updated academic year details in JSON format") @RequestBody @Valid AddAcademicYearDto dto) {
        // Map the DTO to the domain model
        AcademicYear academicYear = academicYearMapper.toModelFromDto(dto);
        academicYear.setId(id); // Ensure the ID is set for the update operation
        AcademicYear updatedAcademicYear = academicYearUseCases.update(academicYear);
        return ResponseEntity.ok(academicYearMapper.toSimpleDto(updatedAcademicYear));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a AcademicYear", description = "Deletes a AcademicYear by their ID.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "AcademicYear deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "AcademicYear not found", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            })
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "ID of the AcademicYear to be deleted", required = true) @PathVariable UUID id) {
        academicYearUseCases.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get AcademicYear by ID", description = "Retrieves a AcademicYear's details by their ID.")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful retrieval",
                            content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AcademicYearDto.class))),
                    @ApiResponse(responseCode = "404", description = "AcademicYear not found", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            })
    public ResponseEntity<AcademicYearSimpleDto> findById(
            @Parameter(description = "ID of the AcademicYear to be retrieved", required = true) @PathVariable UUID id) {
        AcademicYear academicYear = academicYearUseCases.findById(id);
        return ResponseEntity.ok(academicYearMapper.toSimpleDto(academicYear));
    }

    @GetMapping
    @Operation(
            summary = "Get all AcademicYears paginated",
            description = "Retrieves AcademicYears with pagination, search, and sorting.")
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
    public ResponseEntity<CustomPage<AcademicYearSimpleDto>> findAllPaginated(
            @Parameter(description = "Page size", example = "10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Page number (starting from 0)", example = "0") @RequestParam(defaultValue = "0")
            int page,
            @Parameter(description = "Sort field", example = "id") @RequestParam(defaultValue = "id", required = false)
            String sort,
            @Parameter(description = "Sort direction (ASC or DESC)", example = "ASC")
            @RequestParam(defaultValue = "ASC")
            String sortDirection) {
        Pageable pageable = PaginationUtils.createPageable(page, size, sort, sortDirection);
        Page<AcademicYearSimpleDto> academicYears =
                academicYearUseCases.findAllPaginated(pageable).map(academicYearMapper::toSimpleDto);
        return ResponseEntity.ok(PageMapper.toCustomPage(academicYears));
    }

    @GetMapping("/all")
    @Operation(summary = "Get all AcademicYears", description = "Retrieves a list of all AcademicYears without pagination.")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful retrieval",
                            content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AcademicYearDto.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            })
    public ResponseEntity<List<AcademicYearDto>> findAll() {
        List<AcademicYearDto> academicYears = academicYearUseCases.findAll().stream().map(academicYearMapper::toResponseDto).toList();
        return ResponseEntity.ok(academicYears);
    }
}