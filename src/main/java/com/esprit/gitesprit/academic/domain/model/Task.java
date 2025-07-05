package com.esprit.gitesprit.academic.domain.model;

import com.esprit.gitesprit.shared.AbstractAuditingModel;
import com.esprit.gitesprit.users.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Task extends AbstractAuditingModel {
    private UUID id;
    private String description;
    private LocalDate dueDate;
    private Double mark;
    private String comment;
    private boolean done;
    private List<String> branchLinks;
    private GroupStudent groupStudent;

    public void addLink(String link) {
        if (branchLinks == null) {
            branchLinks = new ArrayList<>();
        }
        branchLinks.add(link);
    }

    public void removeSubject(String link) {
        if (branchLinks != null) {
            branchLinks.remove(link);
        }
    }
}
