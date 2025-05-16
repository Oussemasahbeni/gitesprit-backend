package com.esprit.gitesprit.users.domain.service;

import com.esprit.gitesprit.auth.domain.enums.RoleType;
import com.esprit.gitesprit.auth.domain.model.AuthUser;
import com.esprit.gitesprit.auth.domain.port.output.IdentityProvider;
import com.esprit.gitesprit.users.infrastructure.dto.request.UserRequestDto;
import com.esprit.gitesprit.cloudstorage.domain.model.Blob;
import com.esprit.gitesprit.cloudstorage.domain.service.CloudStorage;
import com.esprit.gitesprit.exception.ForbiddenException;
import com.esprit.gitesprit.exception.NotFoundException;


import com.esprit.gitesprit.shared.annotation.DomainService;
import com.esprit.gitesprit.users.domain.enums.NotificationPreference;
import com.esprit.gitesprit.users.domain.model.Role;
import com.esprit.gitesprit.users.domain.model.User;
import com.esprit.gitesprit.users.domain.port.input.UserUseCases;
import com.esprit.gitesprit.users.domain.port.output.Users;
import com.esprit.gitesprit.users.infrastructure.dto.request.UpdateProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@DomainService
@RequiredArgsConstructor
public class UserService implements UserUseCases {

  private final Users users;
  private final CloudStorage cloudStorage;
  private final IdentityProvider idpService;
  @Override
  public User findById(UUID id) {
    return users
        .findById(id)
        .orElseThrow(
            () -> new NotFoundException(NotFoundException.NotFoundExceptionType.USER_NOT_FOUND));
  }

  @Override
  public User updateProfile(UUID id, UpdateProfileRequest updateProfileRequest) {
//    if (!id.equals(updateProfileRequest.id())) {
//      throw new ForbiddenException(ForbiddenException.ForbiddenExceptionType.ACTION_NOT_ALLOWED);
//    }
//    var updatedUser = users.updateProfile(updateProfileRequest);
//    AuthUser authUser = new AuthUser();
//    authUser.setId(id.toString());
//    authUser.setFirstName(updateProfileRequest.firstName());
//    authUser.setLastName(updateProfileRequest.lastName());
//    authUser.setPhoneNumber(updateProfileRequest.phoneNumber());
//      idpService.update(authUser);
//    return updatedUser;
    return null;
  }

  @Override
  public Blob updateProfilePicture(UUID id, MultipartFile file) {
//    User user = findById(id);
//    Blob uploadFile = cloudStorage.uploadFile(file);
//    String oldProfilePicture = user.getProfilePicture();
//    try {
//      user.setProfilePicture(uploadFile.url());
//      users.update(user);
//      if (oldProfilePicture != null) {
//        cloudStorage.deleteFile(oldProfilePicture);
//      }
//      AuthUser authUser = new AuthUser();
//      authUser.setId(id.toString());
//      authUser.setProfilePicture(uploadFile.url());
//        idpService.update(authUser);
//      return uploadFile;
//    } catch (Exception e) {
//      cloudStorage.deleteFile(uploadFile.url());
//      throw e;
//    }
    return null;
  }

  @Override
  public Boolean deleteProfilePicture(UUID id) {
//    try {
//      User user = findById(id);
//      cloudStorage.deleteFile(user.getProfilePicture());
//      user.setProfilePicture(null);
//      users.update(user);
//      AuthUser authUser = new AuthUser();
//      authUser.setId(id.toString());
//      authUser.setProfilePicture(null);
//        idpService.update(authUser);
//      return true;
//
//    } catch (Exception e) {
//      throw new ForbiddenException(ForbiddenException.ForbiddenExceptionType.ACTION_NOT_ALLOWED);
//    }
    return null;
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

  @Override
  public Page<User> findAllPaginated(String search, RoleType roleType, Pageable pageable) {
    return users.findAll(search, pageable, roleType);
  }

  @Override
  public List<User> findAllByRole(RoleType roleType) {
    return users.findAllByRole(roleType);
  }

  @Override
  public User createUser(UserRequestDto requestDto) {
    List<String> groups = List.of();
    List<String> requiredActions = List.of("UPDATE_PASSWORD");
    AuthUser authUser = AuthUser.builder()
            .firstName(requestDto.firstName())
            .lastName(requestDto.lastName())
            .username(requestDto.username())
            .email(requestDto.email())
            .enabled(true)
            .emailVerified(false)
            .build();
    AuthUser createdUser = idpService.create(authUser, requestDto.roles(), groups, requiredActions, true);

    User user = User.builder()
            .id(UUID.fromString(createdUser.getId()))
            .firstName(createdUser.getFirstName())
            .lastName(createdUser.getLastName())
            .username(createdUser.getUsername())
            .email(createdUser.getEmail())
            .address(requestDto.address())
            .phoneNumber(requestDto.phoneNumber())
            .profilePicture(null)
            .locale(null)
            .notificationPreference(NotificationPreference.ALL)
            .build();

    return users.create(user,requestDto.roles());
  }

  @Override
  public Boolean existsByEmail(String email) {
    return users.existsByEmail(email);
  }

  @Override
  public void deleteById(UUID id) {
    User user = findById(id);
    users.deleteById(id);
    idpService.deleteById(user.getId().toString());
  }
}
