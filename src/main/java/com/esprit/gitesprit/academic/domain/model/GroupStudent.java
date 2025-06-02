package com.esprit.gitesprit.academic.domain.model;

import com.esprit.gitesprit.shared.AbstractAuditingModel;
import com.esprit.gitesprit.users.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class GroupStudent extends AbstractAuditingModel {
    private UUID id;
    private User student;
    private Group group;
    private Double individualMark;
    private String individualComment;
    private Double finalMark;

    public Double getFinalMark() {
        if (group == null || group.getSubject() == null || individualMark == null || group.getMark() == null) {
            return null;
        }
        Subject subject = group.getSubject();
        Double groupContribution = (group.getMark() * subject.getGroupMarkPercentage()) / 100.0;
        Double individualContribution = (this.individualMark * subject.getIndividualMarkPercentage()) / 100.0;

        return groupContribution + individualContribution;
    }
}
