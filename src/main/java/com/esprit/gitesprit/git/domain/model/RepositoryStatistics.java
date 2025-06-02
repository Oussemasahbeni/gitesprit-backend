package com.esprit.gitesprit.git.domain.model;

import com.esprit.gitesprit.shared.AbstractAuditingModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepositoryStatistics extends AbstractAuditingModel {
    private String repositoryName;
    private long totalCommits;
    private long totalBranches;
    private long totalTags;
    private Instant lastCommitDate;
    private String lastCommitAuthor;
    private Set<ContributorInfo> contributors;
    private List<BranchInfo> branches;

    // New fields for daily statistics (to be plotted on the graph)
    private Map<LocalDate, Long> dailyNewCommits; // Represents daily total commits (like 'New' issues)
    private Map<LocalDate, Long> dailyMergedCommits; // Represents daily merge commits (like 'Closed' issues)
}
