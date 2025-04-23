package com.esprit.gitesprit.notifications.infrastructure.mapper;

import com.esprit.gitesprit.notifications.domain.model.Notification;
import com.esprit.gitesprit.notifications.infrastructure.dto.response.NotificationDto;
import com.esprit.gitesprit.notifications.infrastructure.entity.NotificationEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class NotificationMapper {
    public abstract NotificationEntity toEntity(Notification notification);

    public abstract Notification toNotification(NotificationEntity notificationEntity);

    public abstract List<NotificationEntity> toEntities(List<Notification> notifications);

    public abstract List<Notification> toNotifications(List<NotificationEntity> notificationEntities);

    public abstract List<NotificationDto> toDtos(List<Notification> notifications);

    public abstract NotificationDto toDto(Notification notification);
}
