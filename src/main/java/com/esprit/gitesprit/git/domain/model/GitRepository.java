package com.esprit.gitesprit.git.domain.model;

import com.esprit.gitesprit.academic.domain.model.Group;
import com.esprit.gitesprit.shared.AbstractAuditingModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class GitRepository extends AbstractAuditingModel {
    private UUID id;
    private String repositoryName;
    private String repositoryPath;
    private Group group;
}
