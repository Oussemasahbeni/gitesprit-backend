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
public class CommitInfo {
    private String hash;
    private String shortMessage;
    private String fullMessage;
    private String authorName;
    private String authorEmail;
    private String committerName;
    private String committerEmail;
    private Instant commitDate;
}
