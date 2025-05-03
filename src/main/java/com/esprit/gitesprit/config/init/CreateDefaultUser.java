package com.esprit.gitesprit.config.init;



import com.esprit.gitesprit.auth.domain.enums.Locale;
import com.esprit.gitesprit.auth.domain.enums.RoleType;
import com.esprit.gitesprit.auth.infra.utils.KeycloakUtils;
import com.esprit.gitesprit.users.infrastructure.entity.RoleEntity;
import com.esprit.gitesprit.users.infrastructure.entity.UserEntity;
import com.esprit.gitesprit.users.infrastructure.repository.UserRepository;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.extern.log4j.Log4j2;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Log4j2
public class CreateDefaultUser {
    @Value("${keycloak.realm}")
    public String realm;

    @Bean
    @Transactional
    public CommandLineRunner run(
            Keycloak keycloak,
            UserRepository userRepository) {
        return args -> {
            log.info("Creating default admin if not exists in Keycloak or Database");
            createDefaultAdmin(keycloak, userRepository);
        };
    }

    private void createDefaultAdmin(Keycloak keycloak, UserRepository userRepository) {
        Locale locale = Locale.ar;
        String adminPhoneNumber = "54750526";
        String adminEmail = "contact@esprit.tn";
        String adminFirstName = "Esprit";
        String adminLastName =  "admin";
        String adminUsername = "admin";

        List<UserRepresentation> keycloakAdminUsers =
                keycloak.realm(realm).users().searchByEmail(adminEmail, true);
        if (!keycloakAdminUsers.isEmpty()) {
            log.info("Admin user found in Keycloak, checking database...");
            Optional<UserEntity> existingAdminInDb = userRepository.findByEmail(adminEmail);
            if (existingAdminInDb.isPresent()) {
                log.info("Admin user already exists in Keycloak and Database.");
            } else {
                log.info("Admin user found in Keycloak but not in Database, adding to Database.");
                UserRepresentation adminRepresentation = keycloakAdminUsers.getFirst();
                UserEntity defaultAdmin =
                        UserEntity.builder()
                                .id(UUID.fromString(adminRepresentation.getId()))
                                .email(adminEmail)
                                .username(adminUsername)
                                .firstName(adminFirstName)
                                .lastName(adminLastName)
                                .locale(locale)
                                .phoneNumber(adminPhoneNumber)
                                .build();

                RoleEntity roleEntity = RoleEntity.builder()
                        .name(RoleType.admin)
                        .user(defaultAdmin)
                        .build();

                defaultAdmin.getRoles().add(roleEntity);
                userRepository.save(defaultAdmin);
                log.info("Admin user added to database from existing Keycloak user.");
            }

        } else {
            log.info("Admin user not found in Keycloak, creating...");
            try {
                UserRepresentation adminRepresentation = new UserRepresentation();
                adminRepresentation.setEmail(adminEmail);
                adminRepresentation.setFirstName(adminFirstName);
                adminRepresentation.setLastName(adminLastName);
                adminRepresentation.setUsername(adminUsername);
                adminRepresentation.setEnabled(true);
                adminRepresentation.setEmailVerified(true);
                adminRepresentation.setRequiredActions(Collections.singletonList("UPDATE_PASSWORD"));
                CredentialRepresentation adminCredentialRepresentation =
                        KeycloakUtils.createPasswordCredentials("Admin123", true);
                adminRepresentation.setCredentials(
                        Collections.singletonList(adminCredentialRepresentation));
                Response adminResponse = keycloak.realm(realm).users().create(adminRepresentation);
                String adminUserId = CreatedResponseUtil.getCreatedId(adminResponse);
                RoleRepresentation adminRole =
                        keycloak.realm(realm).roles().get(String.valueOf(RoleType.admin)).toRepresentation();
                List<RoleRepresentation> adminRoles = List.of( adminRole);
                keycloak.realm(realm).users().get(adminUserId).roles().realmLevel().add(adminRoles);
                UserEntity defaultAdmin =
                        UserEntity.builder()
                                .id(UUID.fromString(adminUserId))
                                .email(adminEmail)
                                .username(adminUsername)
                                .firstName(adminFirstName)
                                .lastName(adminLastName)
                                .locale(locale)
                                .phoneNumber(adminPhoneNumber)
                                .build();
                RoleEntity roleEntity = RoleEntity.builder()
                        .name(RoleType.admin)
                        .user(defaultAdmin)
                        .build();
                if (defaultAdmin.getRoles() == null) {
                    defaultAdmin.setRoles(new ArrayList<>());
                }

                defaultAdmin.getRoles().add(roleEntity);
                userRepository.save(defaultAdmin);
                log.info("Admin user created in Keycloak and Database.");
            } catch (WebApplicationException e) {
                log.error("Error creating default admin user", e);
            }
        }
    }


}