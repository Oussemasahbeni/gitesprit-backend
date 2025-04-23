package com.esprit.gitesprit.users.domain.service;

import com.esprit.gitesprit.auth.domain.model.AuthUser;
import com.esprit.gitesprit.auth.domain.service.IdentityProviderService;
import com.esprit.gitesprit.cloudstorage.domain.model.Blob;
import com.esprit.gitesprit.cloudstorage.domain.service.CloudStorage;
import com.esprit.gitesprit.exception.ForbiddenException;
import com.esprit.gitesprit.exception.NotFoundException;


import com.esprit.gitesprit.shared.annotation.DomainService;
import com.esprit.gitesprit.users.domain.enums.NotificationPreference;
import com.esprit.gitesprit.users.domain.model.User;
import com.esprit.gitesprit.users.domain.port.input.UserUseCases;
import com.esprit.gitesprit.users.domain.port.output.Users;
import com.esprit.gitesprit.users.infrastructure.dto.request.UpdateProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@DomainService
@RequiredArgsConstructor
public class UserService implements UserUseCases {

  private final Users users;
  private final CloudStorage cloudStorage;
  private final IdentityProviderService identityProviderService;
  @Override
  public User findById(UUID id) {
    return users
        .findById(id)
        .orElseThrow(
            () -> new NotFoundException(NotFoundException.NotFoundExceptionType.USER_NOT_FOUND));
  }

  @Override
  public User updateProfile(UUID id, UpdateProfileRequest updateProfileRequest) {
    if (!id.equals(updateProfileRequest.id())) {
      throw new ForbiddenException(ForbiddenException.ForbiddenExceptionType.ACTION_NOT_ALLOWED);
    }
    var updatedUser = users.updateProfile(updateProfileRequest);
    AuthUser authUser = new AuthUser();
    authUser.setId(id.toString());
    authUser.setFirstName(updateProfileRequest.firstName());
    authUser.setLastName(updateProfileRequest.lastName());
    authUser.setPhoneNumber(updateProfileRequest.phoneNumber());
    identityProviderService.update(authUser);
    return updatedUser;
  }

  @Override
  public Blob updateProfilePicture(UUID id, MultipartFile file) {
    User user = findById(id);
    Blob uploadFile = cloudStorage.uploadFile(file);
    String oldProfilePicture = user.getProfilePicture();
    try {
      user.setProfilePicture(uploadFile.url());
      users.update(user);
      if (oldProfilePicture != null) {
        cloudStorage.deleteFile(oldProfilePicture);
      }
      AuthUser authUser = new AuthUser();
      authUser.setId(id.toString());
      authUser.setProfilePicture(uploadFile.url());
      identityProviderService.update(authUser);
      return uploadFile;
    } catch (Exception e) {
      cloudStorage.deleteFile(uploadFile.url());
      throw e;
    }
  }

  @Override
  public Boolean deleteProfilePicture(UUID id) {
    try {
      User user = findById(id);
      cloudStorage.deleteFile(user.getProfilePicture());
      user.setProfilePicture(null);
      users.update(user);
      AuthUser authUser = new AuthUser();
      authUser.setId(id.toString());
      authUser.setProfilePicture(null);
      identityProviderService.update(authUser);
      return true;

    } catch (Exception e) {
      throw new ForbiddenException(ForbiddenException.ForbiddenExceptionType.ACTION_NOT_ALLOWED);
    }
  }

  @Override
  public void updateNotificationPreference(UUID id, NotificationPreference notificationPreference) {
    User user = findById(id);
    users.updateNotificationPreference(id, user, notificationPreference);
  }


  @Override
  public User findCurrentUser(UUID id) {

    return
        users
            .findById(id)
            .orElseThrow(
                () ->
                    new NotFoundException(NotFoundException.NotFoundExceptionType.USER_NOT_FOUND));

  }
}
