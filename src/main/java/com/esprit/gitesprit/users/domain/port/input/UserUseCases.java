package com.esprit.gitesprit.users.domain.port.input;

import aj.org.objectweb.asm.commons.Remapper;
import com.esprit.gitesprit.auth.domain.enums.Role;
import com.esprit.gitesprit.cloudstorage.domain.model.Blob;
import com.esprit.gitesprit.users.domain.enums.NotificationPreference;
import com.esprit.gitesprit.users.domain.model.User;
import com.esprit.gitesprit.users.infrastructure.dto.request.UpdateProfileRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/** Interface defining the use cases for managing users. */
public interface UserUseCases {

    /**
     * Finds a user by their unique identifier.
     *
     * @param id the unique identifier of the user
     * @return the user associated with the given id
     */
    User findById(UUID id);

    /**
     * Updates the user's profile information.
     *
     * @param id the unique identifier of the user
     * @param updateProfileRequest the request containing the updated profile information
     * @return the updated user
     */
    User updateProfile(UUID id, UpdateProfileRequest updateProfileRequest);

    /**
     * Updates the user's profile picture.
     *
     * @param id the unique identifier of the user
     * @param file the new profile picture file
     * @return the updated user
     */
    Blob updateProfilePicture(UUID id, MultipartFile file);

    Boolean deleteProfilePicture(UUID id);

    void updateNotificationPreference(UUID id, NotificationPreference notificationPreference);

    User findCurrentUser(UUID id);

    Page<User> findAllPaginated(String search, Role role, Pageable pageable);
}
