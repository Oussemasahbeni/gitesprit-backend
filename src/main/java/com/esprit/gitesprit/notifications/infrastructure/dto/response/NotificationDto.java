package com.esprit.gitesprit.notifications.infrastructure.dto.response;

import com.esprit.gitesprit.notifications.domain.enums.NotificationType;

import java.time.Instant;
import java.util.Map;

public record NotificationDto(
        String id,
        String userId,
        Boolean isRead,
        Map<String, Object> data,
        NotificationType type,
        Instant createdAt,
        Instant updatedAt) {}
