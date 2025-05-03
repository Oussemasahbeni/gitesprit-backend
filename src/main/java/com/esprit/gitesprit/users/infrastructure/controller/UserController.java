package com.esprit.gitesprit.users.infrastructure.controller;

import com.esprit.gitesprit.auth.domain.enums.Role;
import com.esprit.gitesprit.cloudstorage.domain.model.Blob;
import com.esprit.gitesprit.shared.pagination.CustomPage;
import com.esprit.gitesprit.shared.pagination.PageMapper;
import com.esprit.gitesprit.shared.pagination.PaginationUtils;
import com.esprit.gitesprit.users.domain.enums.NotificationPreference;
import com.esprit.gitesprit.users.domain.model.User;
import com.esprit.gitesprit.users.domain.port.input.UserUseCases;
import com.esprit.gitesprit.users.infrastructure.dto.request.UpdateProfileRequest;
import com.esprit.gitesprit.users.infrastructure.dto.response.UserDto;
import com.esprit.gitesprit.users.infrastructure.mapper.UserMapper;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static com.esprit.gitesprit.shared.AuthUtils.getCurrentAuthenticatedUserId;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/api/v1/users")
@Tag(
        name = "Users",
        description =
                "Operations related to managing users, including retrieving user information, updating user preferences, and updating user profile.")
@RequiredArgsConstructor
public class UserController {
    private final UserUseCases usersUseCases;
    private final UserMapper userMapper;

    @GetMapping("{id}")
    @Operation(summary = "Find user by ID", description = "Retrieve user information by their unique ID.")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful retrieval",
                            content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            })
    public ResponseEntity<UserDto> findById(
            @Parameter(description = "ID of the user to be retrieved", required = true) @PathVariable UUID id) {
        return ResponseEntity.ok(userMapper.toUserDto(usersUseCases.findById(id)));
    }

    @Operation(
            summary = "Find all users",
            description =
                    "Retrieve all users with pagination, sorting, and filtering options. Supports sorting by fields and filtering based on criteria.")
    @GetMapping()
    public ResponseEntity<CustomPage<UserDto>> findAll(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "id", required = false) String sort,
            @RequestParam(defaultValue = "", required = false) String search,
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(required = false) Role role) {
        Pageable pageable = PaginationUtils.createPageable(page, size, sort, sortDirection);

        Page<UserDto> usersPage =
                usersUseCases.findAllPaginated(search, role, pageable).map(userMapper::toUserDto);
        return ResponseEntity.ok(PageMapper.toCustomPage(usersPage));
    }


    @GetMapping("/me")
    @Operation(
            summary = "Get current authenticated user",
            description = "Retrieve information for the currently authenticated user.")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful retrieval",
                            content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            })
    public ResponseEntity<User> findMe() {
        UUID id = getCurrentAuthenticatedUserId();
        return ResponseEntity.ok(usersUseCases.findCurrentUser(id));
    }

    @PatchMapping("/update-profile")
    @Operation(
            summary = "Update user profile",
            description = "Update profile details such as name, email, etc., for the current authenticated user.")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Profile updated successfully",
                            content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            })
    public ResponseEntity<UserDto> updateProfile(@Valid @RequestBody UpdateProfileRequest updatedProfileRequest) {
        UUID id = getCurrentAuthenticatedUserId();
        User user = usersUseCases.updateProfile(id, updatedProfileRequest);
        return new ResponseEntity<>(userMapper.toUserDto(user), HttpStatus.OK);
    }

    @PatchMapping(value = "/profile-picture", consumes = MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Update user profile picture",
            description = "Update the profile picture for the current authenticated user.")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Profile picture updated successfully",
                            content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Blob.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid file format or size", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            })
    public ResponseEntity<Blob> updateProfilePicture(
            @Parameter(description = "Image file to upload as profile picture") @RequestParam("file")
            MultipartFile file) {
        UUID id = getCurrentAuthenticatedUserId();
        Blob blob = usersUseCases.updateProfilePicture(id, file);
        return new ResponseEntity<>(blob, HttpStatus.OK);
    }

    @DeleteMapping("/profile-picture")
    @Operation(
            summary = "Delete user profile picture",
            description = "Deletes the profile picture for the current authenticated user.")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Profile picture deleted successfully",
                            content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Boolean.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No profile picture found to delete",
                            content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            })
    public ResponseEntity<Boolean> deleteProfilePicture() {
        UUID id = getCurrentAuthenticatedUserId();
        return ResponseEntity.ok(usersUseCases.deleteProfilePicture(id));
    }

    @PatchMapping(value = "/notification/{notificationPreference}")
    @Operation(
            summary = "Update user notification preference",
            description = "Update the notification preference for the current authenticated user.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Notification preference updated successfully"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid notification preference value",
                            content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            })
    public ResponseEntity<Void> updateNotificationPreference(
            @Parameter(description = "Notification preference to set", required = true) @PathVariable
            NotificationPreference notificationPreference) {
        UUID id = getCurrentAuthenticatedUserId();
        usersUseCases.updateNotificationPreference(id, notificationPreference);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
