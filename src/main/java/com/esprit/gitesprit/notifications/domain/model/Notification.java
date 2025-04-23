package com.esprit.gitesprit.notifications.domain.model;

import com.esprit.gitesprit.notifications.domain.enums.NotificationType;
import com.esprit.gitesprit.shared.AbstractAuditingModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Notification extends AbstractAuditingModel {
    private UUID id;
    private NotificationType type;
    private Map<String, Object> data;
    private UUID userId;
    private Boolean isRead;
}
