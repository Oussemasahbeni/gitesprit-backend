package com.esprit.gitesprit.academic.infrastructure.controller;

import com.esprit.gitesprit.academic.domain.model.Group;
import com.esprit.gitesprit.academic.domain.port.input.GroupUseCases;
import com.esprit.gitesprit.academic.infrastructure.dto.request.AddGroupDto;
import com.esprit.gitesprit.academic.infrastructure.dto.request.GroupMarkDto;
import com.esprit.gitesprit.academic.infrastructure.dto.response.GroupDto;
import com.esprit.gitesprit.academic.infrastructure.dto.response.GroupSimpleDto;
import com.esprit.gitesprit.academic.infrastructure.mapper.GroupMapper;
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
@RequestMapping("/api/v1/groups")
@Tag(name = "Groups", description = "API for managing groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupUseCases groupUseCases;
    private final GroupMapper groupMapper;

    @PostMapping
    public ResponseEntity<GroupSimpleDto> create(@RequestBody @Valid AddGroupDto dto){
        Group group = groupMapper.toModelFromDto(dto);
        Group savedGroup = groupUseCases.create(group, dto.subjectId());
        return ResponseEntity.ok(groupMapper.toSimpleDto(savedGroup));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupDto> findById(@PathVariable UUID id){
        Group group = groupUseCases.findById(id);
        GroupDto groupDto = groupMapper.toResponseDto(group);
        return ResponseEntity.ok(groupDto);
    }

    @GetMapping
    @Operation(
            summary = "Get all Groups paginated",
            description = "Retrieves Groups with pagination, search, and sorting.")
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
    public ResponseEntity<CustomPage<GroupDto>> findAllPaginated(
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
        Page<GroupDto> groups =
                groupUseCases.findAllPaginated(search, pageable).map(groupMapper::toResponseDto);
        return ResponseEntity.ok(PageMapper.toCustomPage(groups));
    }

    @GetMapping("/all")
    @Operation(summary = "Get all Groups", description = "Retrieves a list of all Groups without pagination.")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful retrieval",
                            content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = GroupSimpleDto.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            })
    public ResponseEntity<List<GroupDto>> findAll() {
        List<GroupDto> groups = groupUseCases.findAll().stream().map(groupMapper::toResponseDto).toList();
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/all/{studentId}")
    public ResponseEntity<List<GroupDto>> findAllByStudentId(@PathVariable UUID studentId) {
        List<GroupDto> groups = groupUseCases.findAllByStudentId(studentId).stream().map(groupMapper::toResponseDto).toList();
        return ResponseEntity.ok(groups);
    }

    @PatchMapping("/assign/{groupId}/{studentId}")
    public ResponseEntity<GroupDto> assignStudent(@PathVariable UUID groupId, @PathVariable UUID studentId) {
        Group group = groupUseCases.assignStudent(groupId, studentId);
        return ResponseEntity.ok(groupMapper.toResponseDto(group));
    }

    @PutMapping("/{groupId}/mark")
    @Operation(summary = "Add or update a mark for a group", description = "Assigns a mark to a group with optional comments")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Mark successfully assigned or updated",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = GroupDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid mark data", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Group not found", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            })
    public ResponseEntity<GroupDto> addMarkToGroup(
            @Parameter(description = "ID of the group to mark", required = true)
            @PathVariable UUID groupId,
            @Valid @RequestBody GroupMarkDto markDto) {

        Group group = groupUseCases.findById(groupId);
        group.setMark(markDto.getMark());
        group.setComment(markDto.getComment());
        Group updatedGroup = groupUseCases.update(group);

        return ResponseEntity.ok(groupMapper.toResponseDto(updatedGroup));
    }
}
