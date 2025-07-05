package com.esprit.gitesprit.academic.infrastructure.controller;

import com.esprit.gitesprit.academic.domain.model.GroupStudent;
import com.esprit.gitesprit.academic.domain.port.input.GroupStudentUseCases;
import com.esprit.gitesprit.academic.infrastructure.dto.request.StudentMarkDto;
import com.esprit.gitesprit.academic.infrastructure.dto.response.GroupStudentDto;
import com.esprit.gitesprit.academic.infrastructure.mapper.GroupStudentMapper;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/groups")
@Tag(name = "Group Students", description = "API for managing student marks in groups")
@RequiredArgsConstructor
public class GroupStudentController {

    private final GroupStudentUseCases groupStudentUseCases;
    private final GroupStudentMapper groupStudentMapper;

    @GetMapping("/group/{groupId}")
    @Operation(summary = "Get all students in a group", description = "Retrieves all students in a specific group with their marks")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful retrieval",
                            content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = GroupStudentDto.class))),
                    @ApiResponse(responseCode = "404", description = "Group not found", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            })
    public ResponseEntity<List<GroupStudentDto>> getStudentsByGroup(
            @Parameter(description = "ID of the group", required = true) @PathVariable UUID groupId) {

        List<GroupStudent> groupStudents = groupStudentUseCases.findByGroupId(groupId);
        List<GroupStudentDto> result = groupStudents.stream()
                .map(groupStudentMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get all groups for a student", description = "Retrieves all groups a student belongs to with their marks")
    public ResponseEntity<List<GroupStudentDto>> getGroupsByStudent(@PathVariable UUID studentId) {
        List<GroupStudent> groupStudents = groupStudentUseCases.findByStudentId(studentId);
        List<GroupStudentDto> result = groupStudents.stream()
                .map(groupStudentMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @PutMapping("/{groupId}/student/{studentId}/mark")
    @Operation(summary = "Add or update a mark for a specific student in a group",
            description = "Assigns an individual mark to a student in a specific group with optional comments")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Mark successfully assigned or updated",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = GroupStudentDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid mark data", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Group or student not found", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            })
    public ResponseEntity<GroupStudentDto> addMarkToStudent(
            @Parameter(description = "ID of the group", required = true) @PathVariable UUID groupId,
            @Parameter(description = "ID of the student", required = true) @PathVariable UUID studentId,
            @Valid @RequestBody StudentMarkDto markDto) {

        GroupStudent groupStudent = groupStudentUseCases.addMarkToGroupStudent(
                groupId, studentId, markDto.getMark(), markDto.getComment());

        return ResponseEntity.ok(groupStudentMapper.toDto(groupStudent));
    }

    @GetMapping("/{groupId}/student/{studentId}")
    @Operation(summary = "Get information for a specific student in a group",
            description = "Retrieves mark and other information for a specific student in a group")
    public ResponseEntity<GroupStudentDto> getStudentInGroup(
            @PathVariable UUID groupId,
            @PathVariable UUID studentId) {

        GroupStudent groupStudent = groupStudentUseCases.findByGroupIdAndStudentId(groupId, studentId);
        return ResponseEntity.ok(groupStudentMapper.toDto(groupStudent));
    }


    @PutMapping("/calculate-mark/{id}")
    public ResponseEntity<GroupStudentDto> calculateMark(@PathVariable UUID id) {
        GroupStudent groupStudent = groupStudentUseCases.calculateMark(id);
        return ResponseEntity.ok(groupStudentMapper.toDto(groupStudent));
    }
}
