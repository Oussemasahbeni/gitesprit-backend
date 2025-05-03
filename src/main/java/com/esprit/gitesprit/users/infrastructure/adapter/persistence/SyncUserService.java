package com.esprit.gitesprit.users.infrastructure.adapter.persistence;


import com.esprit.gitesprit.auth.domain.enums.Role;
import com.esprit.gitesprit.users.domain.model.User;
import com.esprit.gitesprit.users.infrastructure.dto.request.SyncUserRequest;
import com.esprit.gitesprit.users.infrastructure.entity.RoleEntity;
import com.esprit.gitesprit.users.infrastructure.entity.UserEntity;
import com.esprit.gitesprit.users.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

import static com.esprit.gitesprit.config.RabbitmqConfig.SYNC_QUEUE;


@Service
@RequiredArgsConstructor
@Log4j2
public class SyncUserService {

    private final UserRepository userRepository;

    // This method listens to the SYNC_QUEUE and saves the user to the database if it doesn't already exist
    @RabbitListener(queues = SYNC_QUEUE)
    public void receiveMessage(SyncUserRequest message) {
        if (userRepository.existsById(message.id())) {
            log.info("User with id {} already exists", message.id());
            return;
        }

        UserEntity user = UserEntity.builder()
                .id(message.id())
                .email(message.email())
                .username(message.username())
                .firstName(message.firstName())
                .lastName(message.lastName())


                .build();
        if (user.getRoles() == null) {
            user.setRoles(new ArrayList<>());
        }

        if (message.roles() != null && !message.roles().isEmpty()) {
            for (String roleName : message.roles()) {

                Optional<Role> roleOptional = Role.fromString(roleName);

                if (roleOptional.isPresent()) {
                    Role validRoleEnum = roleOptional.get();
                    RoleEntity roleEntity = RoleEntity.builder()
                            .name(validRoleEnum)
                            .user(user)
                            .build();

                    user.getRoles().add(roleEntity);
                }

            }
        }
        userRepository.save(user);

        log.info("User with id {} saved", message.id());
    }
}
