package com.esprit.gitesprit.academic.domain.model;

import com.esprit.gitesprit.shared.AbstractAuditingModel;
import com.esprit.gitesprit.users.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.HashSet;
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
}
