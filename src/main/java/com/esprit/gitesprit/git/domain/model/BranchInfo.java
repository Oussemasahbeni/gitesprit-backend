package com.esprit.gitesprit.git.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchInfo {
    private String name;
    private String latestCommitHash;
    private String latestCommitMessage;
    private String latestCommitAuthor;
    private Instant latestCommitDate;
}
