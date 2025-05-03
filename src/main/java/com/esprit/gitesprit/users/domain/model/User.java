package com.esprit.gitesprit.users.domain.model;

import com.esprit.gitesprit.auth.domain.enums.Locale;
import com.esprit.gitesprit.auth.domain.enums.RoleType;
import com.esprit.gitesprit.shared.AbstractAuditingModel;
import com.esprit.gitesprit.users.domain.enums.Gender;
import com.esprit.gitesprit.users.domain.enums.NotificationPreference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.util.StringUtils.capitalize;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class User extends AbstractAuditingModel {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String phoneNumber;
    private String profilePicture;
    private Gender gender;
    private String address;
    private LocalDate birthDate;
    private List<Role> roles = new ArrayList<>();
    private Locale locale;
    private NotificationPreference notificationPreference;

    public String getFullName() {
        return capitalize(firstName) + " " + capitalize(lastName);
    }
}
