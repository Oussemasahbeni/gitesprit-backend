package com.esprit.gitesprit.users.infrastructure.adapter.persistence;

import com.esprit.gitesprit.auth.domain.enums.Locale;
import com.esprit.gitesprit.auth.domain.enums.RoleType;
import com.esprit.gitesprit.exception.ConflictException;
import com.esprit.gitesprit.exception.NotFoundException;
import com.esprit.gitesprit.shared.annotation.PersistenceAdapter;
import com.esprit.gitesprit.users.domain.enums.NotificationPreference;
import com.esprit.gitesprit.users.domain.model.User;
import com.esprit.gitesprit.users.domain.port.output.Users;
import com.esprit.gitesprit.users.infrastructure.adapter.specifications.UserSpecifications;
import com.esprit.gitesprit.users.infrastructure.dto.request.UpdateProfileRequest;
import com.esprit.gitesprit.users.infrastructure.entity.RoleEntity;
import com.esprit.gitesprit.users.infrastructure.entity.UserEntity;
import com.esprit.gitesprit.users.infrastructure.mapper.UserMapper;
import com.esprit.gitesprit.users.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.StaleObjectStateException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@PersistenceAdapter
@RequiredArgsConstructor
@Transactional
public class UserJpaAdapter implements Users {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public Optional<User> findById(UUID id) {
        UserEntity userEntity = userRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(NotFoundException.NotFoundExceptionType.USER_NOT_FOUND));

        return Optional.of(userMapper.toUser(userEntity));
    }

    @Override
    public User create(User user, List<RoleType> roles) {
        UserEntity userEntity = userMapper.toUserEntity(user);

        if (roles != null && !roles.isEmpty()) {
            userEntity.setRoles(new ArrayList<>());
            for (RoleType role : roles) {
                RoleEntity roleEntity = new RoleEntity();
                roleEntity.setName(role);
                roleEntity.setUser(userEntity);
                userEntity.getRoles().add(roleEntity);
            }
        }
        UserEntity savedUser= userRepository.save(userEntity);
        return userMapper.toUser(savedUser);
    }


    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email).map(userMapper::toUser);
    }

    @Override
    public void deleteById(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    public User update(User user) {
        try {
            UserEntity userEntity = userMapper.toUserEntity(user);
            var savedUser = userRepository.save(userEntity);

            return userMapper.toUser(savedUser);
        } catch (ObjectOptimisticLockingFailureException | StaleObjectStateException ex) {
            throw new ConflictException(ConflictException.ConflictExceptionType.CONFLICT_LOCK_VERSION);
        }
    }

    @Override
    public User updateProfile(UpdateProfileRequest updateProfileRequest) {
        try {
            UserEntity existingUser = userRepository
                    .findById(updateProfileRequest.id())
                    .orElseThrow(() -> new NotFoundException(NotFoundException.NotFoundExceptionType.USER_NOT_FOUND));
            UserEntity user = userMapper.updateUserFromProfileUpdateRequest(updateProfileRequest, existingUser);
            var savedUser = userRepository.save(user);
            return userMapper.toUser(savedUser);
        } catch (ObjectOptimisticLockingFailureException | StaleObjectStateException ex) {
            throw new ConflictException(ConflictException.ConflictExceptionType.CONFLICT_LOCK_VERSION);
        }
    }

    @Override
    public void updateNotificationPreference(UUID id, User user, NotificationPreference notificationPreference) {
        user.setNotificationPreference(notificationPreference);
        this.update(user);
    }

    @Override
    public void updateLocale(String id, Locale locale) {
        UserEntity userEntity = userRepository
                .findById(UUID.fromString(id))
                .orElseThrow(() -> new NotFoundException(NotFoundException.NotFoundExceptionType.USER_NOT_FOUND));
        userEntity.setLocale(locale);
        userRepository.save(userEntity);
    }

    @Override
    public List<User> findAllByRole(RoleType roleType) {
        return userRepository.findAllByRoleType(roleType).stream().map(userMapper::toUser).toList();
    }

    @Override
    public Page<User> findAll(String search, Pageable pageable, RoleType roleType) {
        return userRepository
                .findAll(UserSpecifications.hasCriteria(search, roleType), pageable)
                .map(userMapper::toUser);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

}
