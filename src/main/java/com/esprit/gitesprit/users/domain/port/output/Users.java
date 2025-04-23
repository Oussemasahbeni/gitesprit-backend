package com.esprit.gitesprit.users.domain.port.output;

import com.esprit.gitesprit.auth.domain.enums.Locale;
import com.esprit.gitesprit.auth.domain.model.AuthUser;
import com.esprit.gitesprit.users.domain.enums.NotificationPreference;
import com.esprit.gitesprit.users.domain.model.User;
import com.esprit.gitesprit.users.infrastructure.dto.request.UpdateProfileRequest;

import java.util.Optional;
import java.util.UUID;

public interface Users {

    Optional<User> findById(UUID id);

    void createFromAuthUser(AuthUser user);
    
    Optional<User> findByEmail(String email);

    void deleteById(UUID id);

    User update(User user);

    User updateProfile(UpdateProfileRequest updateProfileRequest);

    void updateNotificationPreference(UUID id, User user, NotificationPreference notificationPreference);

    void updateLocale(String id, Locale locale);

}
