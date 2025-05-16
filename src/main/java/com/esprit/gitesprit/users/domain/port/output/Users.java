package com.esprit.gitesprit.users.domain.port.output;

import com.esprit.gitesprit.auth.domain.enums.Locale;
import com.esprit.gitesprit.auth.domain.enums.RoleType;
import com.esprit.gitesprit.users.domain.enums.NotificationPreference;
import com.esprit.gitesprit.users.domain.model.User;
import com.esprit.gitesprit.users.infrastructure.dto.request.UpdateProfileRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Users {

    Optional<User> findById(UUID id);

    User create(User user, List<RoleType> roles);
    
    Optional<User> findByEmail(String email);

    void deleteById(UUID id);

    User update(User user);

    User updateProfile(UpdateProfileRequest updateProfileRequest);

    void updateNotificationPreference(UUID id, User user, NotificationPreference notificationPreference);

    void updateLocale(String id, Locale locale);

    List<User> findAllByRole(RoleType roleType);

    Page<User> findAll(String search, Pageable pageable, RoleType roleType);

    Boolean existsByEmail(String email);
}
