package com.esprit.gitesprit.academic.infrastructure.controller;

import com.esprit.gitesprit.academic.domain.model.AcademicYear;
import com.esprit.gitesprit.academic.domain.port.input.AcademicYearUseCases;
import com.esprit.gitesprit.academic.infrastructure.dto.request.AddAcademicYearDto;
import com.esprit.gitesprit.academic.infrastructure.mapper.AcademicYearMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<AcademicYear> create(
            @Parameter(description = "Academic year details in JSON format") @RequestBody @Valid AddAcademicYearDto dto) {
        AcademicYear academicYear = academicYearMapper.toModelFromDto(dto);
        AcademicYear createdAcademicYear = academicYearUseCases.create(academicYear);
        return ResponseEntity.ok(createdAcademicYear);
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
    public ResponseEntity<AcademicYear> update(
            @Parameter(description = "ID of the academic year to be updated", required = true) @PathVariable UUID id,
            @Parameter(description = "Updated academic year details in JSON format") @RequestBody @Valid AddAcademicYearDto dto) {
        // Map the DTO to the domain model
        AcademicYear academicYear = academicYearMapper.toModelFromDto(dto);
        academicYear.setId(id); // Ensure the ID is set for the update operation
        AcademicYear updatedAcademicYear = academicYearUseCases.update(academicYear);
        return ResponseEntity.ok(updatedAcademicYear);
    }

    // Additional methods (e.g., findById, deleteById, findAll) can be implemented similarly.
}