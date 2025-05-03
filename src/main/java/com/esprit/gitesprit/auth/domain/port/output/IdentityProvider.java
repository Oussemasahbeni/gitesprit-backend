package com.esprit.gitesprit.auth.domain.port.output;

import com.esprit.gitesprit.auth.domain.enums.Locale;
import com.esprit.gitesprit.auth.domain.enums.RoleType;
import com.esprit.gitesprit.auth.domain.model.AuthUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IdentityProvider {

    Optional<AuthUser> findById(String id);

    Optional<AuthUser> findByEmail(String email);

    List<AuthUser> findByGroup(String groupId);

    Page<AuthUser> findAll(List<String> roles, String search, Pageable pageable);

    AuthUser create(
            AuthUser user,
            List<RoleType> roleTypes,
            List<String> groups,
            List<String> requiredActions,
            boolean sendEmail);

    AuthUser update(AuthUser user);

    Optional<AuthUser> findByUsername(String username);

    /**
     * Example Usage: Optional<AuthUser> user =
     * identityProvider.findByCustomAttribute("username=inspark");
     */
    List<AuthUser> findByCustomAttribute(String query);

    List<AuthUser> findByRole(RoleType roleType);

    void deleteById(String id);

    AuthUser disableUser(String id);

    AuthUser enableUser(String id);

    AuthUser toggle(String userId);

    AuthUser updateLocale(String userId, Locale locale);
}
