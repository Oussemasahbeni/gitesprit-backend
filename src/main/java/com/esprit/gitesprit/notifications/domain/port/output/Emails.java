package com.esprit.gitesprit.notifications.domain.port.output;

import com.esprit.gitesprit.notifications.domain.enums.EmailType;
import jakarta.mail.MessagingException;
import org.springframework.scheduling.annotation.Async;

import java.util.Map;

public interface Emails {
    @Async
    void sendEmail(String to, EmailType emailType, Map<String, Object> properties) throws MessagingException;
}
