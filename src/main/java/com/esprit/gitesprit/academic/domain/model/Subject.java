package com.esprit.gitesprit.academic.domain.model;

import com.esprit.gitesprit.shared.AbstractAuditingModel;
import com.esprit.gitesprit.users.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Subject extends AbstractAuditingModel {
    private UUID id;
    private String name;
    private Classroom classroom;
    private User teacher;
    private List<Group> groups;
    private Double groupMarkPercentage; // Percentage of the final mark attributed to the group mark
    private Double individualMarkPercentage; // Percentage of the final mark attributed to individual contributions

    public void addGroup(Group group) {
        if (groups == null) {
            groups = new ArrayList<>();
        }
        groups.add(group);
    }

    public void removeGroup(Group group) {
        if (groups != null) {
            groups.remove(group);
        }
    }

    // Ensures the percentages always add up to 100%
    public void validateMarkPercentages() {
        if (groupMarkPercentage == null) {
            groupMarkPercentage = 50.0; // Default to 50%
        }

        if (individualMarkPercentage == null) {
            individualMarkPercentage = 100.0 - groupMarkPercentage;
        }

        // Ensure they always add up to 100%
        double sum = groupMarkPercentage + individualMarkPercentage;
        if (Math.abs(sum - 100.0) > 0.01) { // Allow for small floating point errors
            groupMarkPercentage = (groupMarkPercentage / sum) * 100.0;
            individualMarkPercentage = 100.0 - groupMarkPercentage;
        }
    }
}
