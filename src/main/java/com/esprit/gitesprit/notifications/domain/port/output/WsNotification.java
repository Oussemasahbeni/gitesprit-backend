package com.esprit.gitesprit.notifications.domain.port.output;

public interface WsNotification {

    void send(String userId, Object payload);
}
