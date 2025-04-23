package com.esprit.gitesprit.auth.domain.service;

import com.esprit.gitesprit.auth.domain.enums.Locale;
import com.esprit.gitesprit.auth.domain.enums.SyncActions;
import com.esprit.gitesprit.auth.infra.dto.request.KcSyncRequest;
import com.esprit.gitesprit.exception.ExistsException;
import com.esprit.gitesprit.users.infrastructure.entity.UserEntity;
import com.esprit.gitesprit.users.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class KcSyncService {

    private static final Locale DEFAULT_LOCALE = Locale.ar;
    private final UserRepository userRepository;

    public Boolean syncUser(KcSyncRequest request) {

        if (request.action().equals(SyncActions.DELETE_ACCOUNT)) {
            userRepository.deleteById(UUID.fromString(request.id()));
            return true;
        }

        if (request.action().equals(SyncActions.REGISTER)) {

            userRepository.findById(UUID.fromString(request.id())).ifPresent(user -> {
                throw new ExistsException(ExistsException.ExistsExceptionType.USER_ALREADY_EXISTS);
            });
            UserEntity newUser = UserEntity.builder()
                    .id(UUID.fromString(request.id()))
                    .email(request.email())
                    .firstName(request.firstName())
                    .lastName(request.lastName())
                    .username(request.username())
                    .locale(DEFAULT_LOCALE)
                    .phoneNumber(request.phoneNumber())
                    .build();
            if (request.profilePicture() != null) {
                newUser.setProfilePicture(request.profilePicture());
            }
            userRepository.save(newUser);
        }

        return true;
    }
}
