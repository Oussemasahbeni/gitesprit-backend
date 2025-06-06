package com.esprit.gitesprit.notifications.infrastructure.adapter.persistence;

import com.esprit.gitesprit.notifications.infrastructure.entity.NotificationEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class NotificationSpecification {

    private NotificationSpecification() {}

    public static Specification<NotificationEntity> hasUserId(UUID userId) {
        return (root, query, cb) -> cb.equal(root.get("userId"), userId);
    }
}
