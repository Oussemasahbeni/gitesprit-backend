package com.esprit.gitesprit.auth.domain.service;

import com.esprit.gitesprit.auth.domain.enums.Locale;
import com.esprit.gitesprit.auth.domain.enums.Role;
import com.esprit.gitesprit.auth.domain.model.AuthUser;
import com.esprit.gitesprit.auth.domain.port.input.IdentityProviderUseCases;
import com.esprit.gitesprit.auth.domain.port.output.IdentityProvider;
import com.esprit.gitesprit.exception.NotFoundException;
import com.esprit.gitesprit.shared.annotation.DomainService;
import com.esprit.gitesprit.users.domain.port.output.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@DomainService
@Slf4j
@RequiredArgsConstructor
public class IdentityProviderService implements IdentityProviderUseCases {

    private final IdentityProvider iamUserRepository;
    private final Users users;

    @Override
    public AuthUser createUser(AuthUser authUser) {
        List<String> groups = List.of("users");
        List<String> requiredActions = List.of("UPDATE_PASSWORD");
        List<Role> roles = List.of(Role.student);
        AuthUser createdUser = iamUserRepository.create(authUser, roles, groups, requiredActions, true, false, true);
        users.createFromAuthUser(createdUser);
        return createdUser;
    }

    @Override
    public AuthUser createAdmin(AuthUser authUser) {
        List<String> groups = List.of("admins");
        List<String> requiredActions = List.of("UPDATE_PASSWORD");
        List<Role> roles = List.of(Role.admin);
        AuthUser createdUser = iamUserRepository.create(authUser, roles, groups, requiredActions, true, true, true);
        users.createFromAuthUser(createdUser);
        return createdUser;
    }



    @Override
    public AuthUser update(AuthUser authUser) {
        return iamUserRepository.update(authUser);
    }

    @Override
    public AuthUser findById(String id) {
        return iamUserRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(NotFoundException.NotFoundExceptionType.USER_NOT_FOUND));
    }

    @Override
    public AuthUser findByEmail(String email) {
        return iamUserRepository
                .findByEmail(email)
                .orElseThrow(() -> new NotFoundException(NotFoundException.NotFoundExceptionType.USER_NOT_FOUND));
    }

    @Override
    public Optional<AuthUser> findByUsername(String username) {
        return iamUserRepository.findByUsername(username);
    }

    /**
     * Example Usage: Optional<AuthUser> user =
     * identityProvider.findByCustomAttribute("username=inspark");
     */
    @Override
    public List<AuthUser> findByCustomAttribute(String query) {
        return iamUserRepository.findByCustomAttribute(query);
    }

    @Override
    public List<AuthUser> findByRole(Role role) {
        return iamUserRepository.findByRole(role);
    }

    @Override
    public List<AuthUser> findByGroup(String role) {
        return iamUserRepository.findByGroup(role);
    }

    @Override
    public Page<AuthUser> findAll(List<String> roles, String search, Pageable pageable) {
        return iamUserRepository.findAll(roles, search, pageable);
    }

    /**
     * Enables a user's active status.
     *
     * @param userId The ID of the user to enable.
     * @return The user with the enabled status.
     */
    @Override
    public AuthUser enableUser(String userId) {
        return this.iamUserRepository.enableUser(userId);
    }

    /**
     * Disables a user's active status.
     *
     * @param userId The ID of the user to disable.
     * @return The user with the disabled status.
     */
    @Override
    public AuthUser disableUser(String userId) {
        return this.iamUserRepository.disableUser(userId);
    }

    //  @Override
    //  public AuthUser toggleUserStatus(String userId) {
    //    return iamUserRepository.toggle(userId);
    //  }

    @Override
    public void deleteById(String userId) {
        iamUserRepository.deleteById(userId);
    }

    public AuthUser toggleUserStatus(String userId) {
        return iamUserRepository.toggle(userId);
    }

    @Override
    public AuthUser updateLocale(String userId, Collection<? extends GrantedAuthority> authorities, Locale locale) {
        AuthUser user = iamUserRepository.updateLocale(userId, locale);
        if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_admin"))) {
            users.updateLocale(user.getId(), locale);
        } else {
            users.updateLocale(user.getId(), locale);
        }
        return user;
    }
}
