package com.esprit.gitesprit.notifications.infrastructure.controller;

import com.esprit.gitesprit.notifications.domain.model.Notification;
import com.esprit.gitesprit.notifications.domain.port.input.NotificationUseCases;
import com.esprit.gitesprit.notifications.infrastructure.dto.response.NotificationDto;
import com.esprit.gitesprit.notifications.infrastructure.mapper.NotificationMapper;
import com.esprit.gitesprit.shared.pagination.CustomPage;
import com.esprit.gitesprit.shared.pagination.PageMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.esprit.gitesprit.shared.AuthUtils.getCurrentAuthenticatedUserId;
import static com.esprit.gitesprit.shared.pagination.PaginationUtils.createPageable;

@RestController
@RequestMapping("/api/v1/notifications")
@Tag(
        name = "Notifications",
        description =
                "Operations related to managing notifications, including marking as read, deleting, and retrieving notifications.")
@RequiredArgsConstructor
public class NotificationsController {

    private final NotificationUseCases notificationsUseCases;
    private final NotificationMapper notificationMapper;

    @GetMapping("/all")
    @Operation(
            summary = "Get all notifications (paginated)",
            description = "Retrieves all notifications for the current user with pagination.")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful retrieval",
                            content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CustomPage.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            })
    public ResponseEntity<CustomPage<NotificationDto>> getAllNotifications(
            @Parameter(description = "Page size", example = "10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Page number (starting from 0)", example = "0") @RequestParam(defaultValue = "0")
            int page) {
        UUID id = getCurrentAuthenticatedUserId();
        Pageable pageable = createPageable(page, size, "createdAt", "DESC");
        Page<Notification> notifications = this.notificationsUseCases.getAllByUserId(id, pageable);
        Page<NotificationDto> notificationDtos = notifications.map(notificationMapper::toDto);
        return new ResponseEntity<>(PageMapper.toCustomPage(notificationDtos), HttpStatus.OK);
    }

    @GetMapping("/unread-count")
    @Operation(
            summary = "Get unread notifications count",
            description = "Retrieves the count of unread notifications for the current user.")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful retrieval",
                            content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Long.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            })
    public ResponseEntity<Long> getUnreadNotificationsCount() {
        UUID id = getCurrentAuthenticatedUserId();
        return new ResponseEntity<>(this.notificationsUseCases.getUnreadNotificationsCount(id), HttpStatus.OK);
    }

    @PatchMapping("/{notificationId}")
    @Operation(summary = "Mark notification as read", description = "Mark a specific notification as read by its ID.")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Notification marked as read",
                            content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Notification.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Notification not found", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            })
    public ResponseEntity<Notification> markAsRead(
            @Parameter(description = "ID of the notification to mark as read", required = true) @PathVariable
            UUID notificationId) {
        Notification updatedNotification = this.notificationsUseCases.markAsRead(notificationId);
        return new ResponseEntity<>(updatedNotification, HttpStatus.OK);
    }

    @PatchMapping("/all")
    @Operation(
            summary = "Mark all notifications as read",
            description = "Mark all notifications for the current user as read.")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "All notifications marked as read",
                            content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Boolean.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            })
    public ResponseEntity<Boolean> markAllAsRead() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        return new ResponseEntity<>(this.notificationsUseCases.markAllAsRead(UUID.fromString(id)), HttpStatus.OK);
    }

    @DeleteMapping("/{notificationId}")
    @Operation(summary = "Delete notification", description = "Delete a specific notification by its ID.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "Notification deleted successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Notification not found", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            })
    public ResponseEntity<Void> deleteNotification(
            @Parameter(description = "ID of the notification to delete", required = true) @PathVariable
            UUID notificationId) {
        this.notificationsUseCases.deleteById(notificationId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/all")
    @Operation(summary = "Delete all notifications", description = "Delete all notifications for the current user.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "All notifications deleted successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
            })
    public ResponseEntity<Void> deleteAllNotifications() {
        UUID id = getCurrentAuthenticatedUserId();
        this.notificationsUseCases.deleteAll(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
