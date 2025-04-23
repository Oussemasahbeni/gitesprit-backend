package com.esprit.gitesprit.auth.infra.adapter;

import com.esprit.gitesprit.auth.domain.enums.Locale;
import com.esprit.gitesprit.auth.domain.enums.Role;
import com.esprit.gitesprit.auth.domain.model.AuthUser;
import com.esprit.gitesprit.auth.domain.port.output.IdentityProvider;
import com.esprit.gitesprit.auth.infra.mapper.AuthMapper;
import com.esprit.gitesprit.exception.BadRequestException;
import com.esprit.gitesprit.exception.NotFoundException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Make sure to enable <b>view-realm</b> and <b>manage-users</b> roles for the client service
 * account in keycloak
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KeycloakUserAdapter implements IdentityProvider {

    private static final String DEFAULT_LOCALE = "ar";
    private final Keycloak keycloak;
    private final AuthMapper authMapper;

    @Value("${keycloak.realm}")
    public String realm;

    @Value("${application.frontend.url}")
    public String frontendUrl;

    @Override
    public Optional<AuthUser> findById(String id) {
        UserRepresentation userRepresentation =
                keycloak.realm(realm).users().get(id).toRepresentation();

        if (userRepresentation == null) {
            return Optional.empty();
        }
        return Optional.of(mapUser(userRepresentation));
    }

    @Override
    public List<AuthUser> findByGroup(String groupName) {

        List<GroupRepresentation> groups = keycloak.realm(realm).groups().groups();
        GroupRepresentation group = groups.stream()
                .filter(g -> g.getName().equals(groupName))
                .findFirst()
                .orElseThrow();

        List<UserRepresentation> users =
                keycloak.realm(realm).groups().group(group.getId()).members();
        List<AuthUser> authUsers = new ArrayList<>();
        for (UserRepresentation user : users) {
            AuthUser mappedUser = mapUser(user);
            authUsers.add(mappedUser);
        }
        return authUsers;
    }

    @Override
    public Page<AuthUser> findAll(List<String> roles, String search, Pageable pageable) {

        RealmResource realmResource = keycloak.realm(realm);

        List<UserRepresentation> users =
                realmResource.users().search(search, pageable.getPageNumber(), pageable.getPageSize());
        List<AuthUser> authUsers = new ArrayList<>();
        for (UserRepresentation user : users) {
            processUserRoles(user, roles, authUsers, realmResource);
        }
        sortAuthUsers(authUsers, pageable.getSort());
        int totalElements = realmResource.users().count();
        return new PageImpl<>(authUsers, pageable, totalElements);
    }

    /** Process user roles and add to the list of auth users if the user has the required roles */
    private void processUserRoles(
            UserRepresentation user, List<String> roles, List<AuthUser> authUsers, RealmResource realmResource) {
        List<RoleRepresentation> userRoles =
                realmResource.users().get(user.getId()).roles().realmLevel().listEffective();
        List<Role> roleEnums = new ArrayList<>();
        for (RoleRepresentation roleRepresentation : userRoles) {
            if (roleRepresentation.getName().equals("default-roles-" + realm)) {
                Set<RoleRepresentation> defaultRoles =
                        realmResource.rolesById().getRoleComposites(roleRepresentation.getId());
                for (RoleRepresentation defaultRole : defaultRoles) {
                    roleEnums.add(Role.fromString(defaultRole.getName()).orElse(null));
                }
            } else {
                roleEnums.add(Role.fromString(roleRepresentation.getName()).orElse(null));
            }
        }
        roleEnums = roleEnums.stream().filter(Objects::nonNull).toList();

        if (!roles.isEmpty() && roleEnums.stream().anyMatch(role -> roles.contains(role.toString()))) {
            AuthUser mappedUser = mapUser(user);
            authUsers.add(mappedUser);
        }
    }

    private void sortAuthUsers(List<AuthUser> authUsers, Sort sort) {
        if (sort.isSorted()) {
            sort.forEach(order -> {
                Comparator<AuthUser> comparator;
                switch (order.getProperty()) {
                    case "email" -> comparator = Comparator.comparing(AuthUser::getEmail);
                    case "firstName" -> comparator = Comparator.comparing(AuthUser::getFirstName);
                    case "lastName" -> comparator = Comparator.comparing(AuthUser::getLastName);
                    case "createdAt" -> comparator = Comparator.comparing(AuthUser::getCreatedAt);
                    default -> comparator = Comparator.comparing(AuthUser::getId);
                }
                if (order.getDirection() == Sort.Direction.DESC) {
                    comparator = comparator.reversed();
                }
                authUsers.sort(comparator);
            });
        }
    }

    @Override
    public Optional<AuthUser> findByEmail(String email) {
        List<UserRepresentation> users = keycloak.realm(realm).users().searchByEmail(email, true);
        return getAuthUser(users);
    }

    @NotNull
    private Optional<AuthUser> getAuthUser(List<UserRepresentation> users) {
        if (!users.isEmpty()) {
            var user = users.getFirst();
            return Optional.of(this.mapUser(user));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<AuthUser> findByUsername(String username) {
        List<UserRepresentation> users = keycloak.realm(realm).users().searchByUsername(username, true);
        return getAuthUser(users);
    }

    /**
     * Example Usage: Optional<AuthUser> user =
     * identityProvider.findByCustomAttribute("username=inspark");
     */
    @Override
    public List<AuthUser> findByCustomAttribute(String query) {
        List<UserRepresentation> users = keycloak.realm(realm).users().searchByAttributes(query);
        List<AuthUser> authUsers = new ArrayList<>();
        for (UserRepresentation user : users) {
            AuthUser mappedUser = mapUser(user);
            authUsers.add(mappedUser);
        }
        return authUsers;
    }

    /**
     * This method requires the role <b>view-realm</b> to be enabled for the client service accounts
     * roles in keycloak
     */
    @Override
    public List<AuthUser> findByRole(Role role) {
        List<UserRepresentation> users =
                keycloak.realm(realm).roles().get(role.name()).getUserMembers();
        List<AuthUser> authUsers = new ArrayList<>();
        for (UserRepresentation user : users) {
            AuthUser mappedUser = mapUser(user);
            authUsers.add(mappedUser);
        }
        return authUsers;
    }

    @Override
    public void deleteById(String id) {
        log.info("Deleting user with id: {}", id);
        try {
            keycloak.realm(realm).users().delete(id);
            log.info("User with id: {} deleted successfully", id);
        } catch (WebApplicationException e) {
            log.error("Error deleting user with id: {}", id, e);
            throw new NotFoundException(NotFoundException.NotFoundExceptionType.USER_NOT_FOUND);
        }
    }

    @Override
    public AuthUser create(
            AuthUser authUser,
            List<Role> roles,
            List<String> groups,
            List<String> requiredActions,
            boolean enabled,
            boolean emailVerified,
            boolean sendUpdatePasswordEmail) {
        log.info("Creating user with email: {}", authUser.getEmail());
        try {
            Locale locale = Locale.ar;
            UserRepresentation userRepresentation = authMapper.toUserRepresentation(authUser);
            Map<String, List<String>> attributes = new HashMap<>();
            attributes.put("phoneNumber", List.of(authUser.getPhoneNumber()));
            attributes.put("locale", List.of(locale.name()));

            if (authUser.getProfilePicture() != null) {
                attributes.put("profilePicture", List.of(authUser.getProfilePicture()));
            }
            userRepresentation.setAttributes(attributes);
            userRepresentation.setEnabled(enabled);
            userRepresentation.setEmailVerified(emailVerified);
            userRepresentation.setUsername(authUser.getUsername());
            userRepresentation.setGroups(groups);
            userRepresentation.setRequiredActions(requiredActions);
            Response response = keycloak.realm(realm).users().create(userRepresentation);
            String userId = CreatedResponseUtil.getCreatedId(response);
            if (roles != null) {
                roles.forEach(role -> {
                    RoleRepresentation roleRepresentation =
                            keycloak.realm(realm).roles().get(role.toString()).toRepresentation();
                    keycloak.realm(realm)
                            .users()
                            .get(userId)
                            .roles()
                            .realmLevel()
                            .add(Collections.singletonList(roleRepresentation));
                });
            }
            // Add required action to set password

            if (sendUpdatePasswordEmail) {
                keycloak.realm(realm)
                        .users()
                        .get(userId)
                        .executeActionsEmail(
                                "agriculture-web-app", frontendUrl, Collections.singletonList("UPDATE_PASSWORD"));
            }

            log.info("User with email: {} created successfully", authUser.getEmail());

            return this.findById(userId)
                    .orElseThrow(() -> new NotFoundException(NotFoundException.NotFoundExceptionType.USER_NOT_FOUND));
        } catch (WebApplicationException e) {
            log.error("Error creating user with email: {}", authUser.getEmail(), e);
            throw new BadRequestException(BadRequestException.BadRequestExceptionType.INVALID_REQUEST);
        }
    }

    @Override
    public AuthUser update(AuthUser user) {
        AuthUser oldUser = this.findById(user.getId())
                .orElseThrow(() -> new NotFoundException(NotFoundException.NotFoundExceptionType.USER_NOT_FOUND));

        AuthUser newUser = authMapper.partialUpdate(user, oldUser);
        UserRepresentation userRepresentation = authMapper.toUserRepresentation(newUser);
        if (user.getLocale() != null) {
            userRepresentation.singleAttribute("locale", user.getLocale().name());
        }
        if (user.getPhoneNumber() != null) {
            userRepresentation.singleAttribute("phoneNumber", user.getPhoneNumber());
        }
        if (user.getProfilePicture() != null) {
            userRepresentation.singleAttribute("profilePicture", user.getProfilePicture());
        }
        keycloak.realm(realm).users().get(user.getId()).update(userRepresentation);

        return this.findById(user.getId())
                .orElseThrow(() -> new NotFoundException(NotFoundException.NotFoundExceptionType.USER_NOT_FOUND));
    }

    @Override
    public AuthUser enableUser(String userId) {
        UserRepresentation userRepresentation =
                keycloak.realm(realm).users().get(userId).toRepresentation();
        userRepresentation.setEnabled(true);
        keycloak.realm(realm).users().get(userRepresentation.getId()).update(userRepresentation);
        UserRepresentation userRepresentationUpdated =
                keycloak.realm(realm).users().get(userRepresentation.getId()).toRepresentation();
        return authMapper.toAuthUser(userRepresentationUpdated);
    }

    @Override
    public AuthUser disableUser(String userId) {
        UserRepresentation userRepresentation =
                keycloak.realm(realm).users().get(userId).toRepresentation();
        userRepresentation.setEnabled(false);
        keycloak.realm(realm).users().get(userRepresentation.getId()).update(userRepresentation);
        UserRepresentation userRepresentationUpdated =
                keycloak.realm(realm).users().get(userRepresentation.getId()).toRepresentation();
        return authMapper.toAuthUser(userRepresentationUpdated);
    }

    @Override
    public AuthUser toggle(String userId) {
        UserRepresentation userRepresentation =
                keycloak.realm(realm).users().get(userId).toRepresentation();
        userRepresentation.setEnabled(!userRepresentation.isEnabled());
        keycloak.realm(realm).users().get(userRepresentation.getId()).update(userRepresentation);
        UserRepresentation userRepresentationUpdated =
                keycloak.realm(realm).users().get(userRepresentation.getId()).toRepresentation();
        return authMapper.toAuthUser(userRepresentationUpdated);
    }

    @Override
    public AuthUser updateLocale(String userId, Locale locale) {
        UserRepresentation userRepresentation =
                keycloak.realm(realm).users().get(userId).toRepresentation();
        userRepresentation.singleAttribute("locale", locale.name());
        keycloak.realm(realm).users().get(userRepresentation.getId()).update(userRepresentation);
        UserRepresentation userRepresentationUpdated =
                keycloak.realm(realm).users().get(userRepresentation.getId()).toRepresentation();
        return authMapper.toAuthUser(userRepresentationUpdated);
    }

    private List<Role> getUserRoles(String userId) {
        List<RoleRepresentation> realmRoles =
                keycloak.realm(realm).users().get(userId).roles().realmLevel().listEffective();
        return realmRoles.stream()
                .map(RoleRepresentation::getName)
                .filter(roleName ->
                        Arrays.asList("admin", "user", "super_admin").contains(roleName))
                .map(Role::valueOf)
                .toList();
    }

    private AuthUser mapUser(UserRepresentation userRepresentation) {
        List<Role> roles = getUserRoles(userRepresentation.getId());
        AuthUser authUser = authMapper.toAuthUser(userRepresentation);
        String locale = userRepresentation.getAttributes().getOrDefault("locale", List.of(DEFAULT_LOCALE)).stream()
                .findFirst()
                .orElse(null);
        authUser.setLocale(Locale.valueOf(locale));
        authUser.setPhoneNumber(userRepresentation.getAttributes().getOrDefault("phoneNumber", List.of()).stream()
                .findFirst()
                .orElse(null));
        authUser.setProfilePicture(userRepresentation.getAttributes().getOrDefault("profilePicture", List.of()).stream()
                .findFirst()
                .orElse(null));

        authUser.setRoles(roles);
        return authUser;
    }
}
