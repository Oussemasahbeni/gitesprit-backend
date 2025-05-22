package com.esprit.gitesprit.git;

import com.esprit.gitesprit.git.domain.model.BranchInfo;
import com.esprit.gitesprit.git.domain.model.CommitInfo;
import com.esprit.gitesprit.git.domain.model.ContributorInfo;
import com.esprit.gitesprit.git.domain.model.RepositoryStatistics;
import com.esprit.gitesprit.git.infrastructure.entity.GitRepositoryEntity;
import com.esprit.gitesprit.git.infrastructure.repository.GitRepositoryRepository;
import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class GitRepositoryService {

    private static final Logger log = LoggerFactory.getLogger(GitRepositoryService.class);

    @Value("${git.server.repos.path}")
    private String repositoriesBasePath;

    private final GitRepositoryRepository gitRepositoryRepository;

    // Helper method to open a JGit Repository object
    private Optional<Repository> openJGitRepository(String repoName) {
        String fullRepoName = repoName;
        if (!fullRepoName.endsWith(Constants.DOT_GIT_EXT)) {
            fullRepoName += Constants.DOT_GIT_EXT;
        }

        Optional<GitRepositoryEntity> repoEntity = gitRepositoryRepository.findByRepositoryName(fullRepoName);
        if (repoEntity.isPresent()) {
            File repoDir = new File(repoEntity.get().getRepositoryPath());
            if (repoDir.exists() && repoDir.isDirectory()) {
                try {
                    return Optional.of(new FileRepositoryBuilder()
                            .setGitDir(repoDir)
                            .readEnvironment()
                            .findGitDir()
                            .setMustExist(true)
                            .build());
                } catch (IOException e) {
                    log.error("Failed to open JGit repository for '{}' at {}: {}", fullRepoName, repoDir.getAbsolutePath(), e.getMessage());
                    return Optional.empty();
                }
            } else {
                log.warn("Repository '{}' found in DB but not on filesystem at {}.", fullRepoName, repoDir.getAbsolutePath());
                // Consider cleaning up DB entry here if filesystem is canonical source
                return Optional.empty();
            }
        } else {
            log.warn("Repository '{}' not found in database.", fullRepoName);
            return Optional.empty();
        }
    }


    public boolean createRepository(String repoName) throws IOException, GitAPIException {
        String fullRepoName = repoName;
        if (!fullRepoName.endsWith(Constants.DOT_GIT_EXT)) {
            fullRepoName += Constants.DOT_GIT_EXT;
        }

        Optional<GitRepositoryEntity> existingDbRepo = gitRepositoryRepository.findByRepositoryName(fullRepoName);
        if (existingDbRepo.isPresent()) {
            log.info("Repository '{}' already exists in database.", fullRepoName);
            File repoDirFromDb = new File(existingDbRepo.get().getRepositoryPath());
            if (repoDirFromDb.exists()) {
                return false; // Repository already exists both in DB and on filesystem
            } else {
                log.warn("Inconsistency: Repository '{}' found in DB but not on filesystem. Path: {}. Attempting to remove from DB.", fullRepoName, existingDbRepo.get().getRepositoryPath());
                gitRepositoryRepository.delete(existingDbRepo.get()); // Clean up inconsistent DB entry
                // Proceed to create as if it didn't exist, as it's missing from FS
            }
        }

        File repoDir = new File(repositoriesBasePath, fullRepoName);

        if (repoDir.exists()) {
            // Check if it's a valid Git repo before adding to DB
            try (Repository checkRepo = new FileRepositoryBuilder().setGitDir(repoDir).readEnvironment().findGitDir().build()) {
                log.info("Repository '{}' exists on filesystem but not in DB. Adding to DB.", fullRepoName);
                GitRepositoryEntity entity = new GitRepositoryEntity();
                entity.setRepositoryName(fullRepoName);
                entity.setRepositoryPath(repoDir.getAbsolutePath());
                gitRepositoryRepository.save(entity);
                return false; // Already exists on filesystem
            } catch (IOException e) {
                log.error("Directory {} exists but is not a valid Git repository. Unable to add to DB.", repoDir.getAbsolutePath(), e);
                return false; // Not a valid Git repo, can't manage it.
            }
        }

        // Create a new bare repository on filesystem
        try (Git git = Git.init().setDirectory(repoDir).setBare(true).call()) {
            log.info("Created new bare repository on filesystem: {}", repoDir.getAbsolutePath());

            // Save to database
            GitRepositoryEntity newRepo = new GitRepositoryEntity();
            newRepo.setRepositoryName(fullRepoName);
            newRepo.setRepositoryPath(repoDir.getAbsolutePath());
            gitRepositoryRepository.save(newRepo);
            log.info("Saved repository to database: {}", fullRepoName);
            return true;
        } catch (GitAPIException e) {
            log.error("Failed to create repository '{}': {}", fullRepoName, e.getMessage(), e);
            throw e; // Re-throw for upstream handling
        }
    }

    public List<String> listRepositories() {
        return gitRepositoryRepository.findAll().stream()
                .map(GitRepositoryEntity::getRepositoryName)
                .collect(Collectors.toList());
    }

    public Path getRepositoryPath(String repoName) {
        String fullRepoName = repoName;
        if (!fullRepoName.endsWith(Constants.DOT_GIT_EXT)) {
            fullRepoName += Constants.DOT_GIT_EXT;
        }

        Optional<GitRepositoryEntity> repoEntity = gitRepositoryRepository.findByRepositoryName(fullRepoName);
        if (repoEntity.isPresent()) {
            return Paths.get(repoEntity.get().getRepositoryPath());
        } else {
            log.warn("Repository '{}' not found in database for path lookup. Falling back to base path.", fullRepoName);
            return Paths.get(repositoriesBasePath, fullRepoName);
        }
    }

    public Optional<GitRepositoryEntity> getRepositoryEntityByName(String repoName) {
        String fullRepoName = repoName;
        if (!fullRepoName.endsWith(Constants.DOT_GIT_EXT)) {
            fullRepoName += Constants.DOT_GIT_EXT;
        }
        return gitRepositoryRepository.findByRepositoryName(fullRepoName);
    }

    /**
     * Retrieves comprehensive statistics for a given repository.
     * @param repoName The name of the repository (e.g., "myrepo" or "myrepo.git").
     * @return RepositoryStatistics object or null if repository not found/accessible.
     */
    public RepositoryStatistics getRepositoryStatistics(String repoName) {
        try (Repository repository = openJGitRepository(repoName).orElse(null)) {
            if (repository == null) {
                log.warn("Repository '{}' not found or could not be opened.", repoName);
                return null;
            }

            try (Git git = new Git(repository)) {
                // Total Commits
                Iterable<RevCommit> allCommitsIterable = git.log().all().call();
                List<RevCommit> commitList = StreamSupport.stream(allCommitsIterable.spliterator(), false)
                        .collect(Collectors.toList());
                long totalCommits = commitList.size();

                // Last Commit Info
                RevCommit lastCommit = null;
                if (!commitList.isEmpty()) {
                    lastCommit = commitList.get(0); // Assuming 'all()' gives latest first by default
                }

                // Branches
                List<Ref> branches = git.branchList().setListMode(org.eclipse.jgit.api.ListBranchCommand.ListMode.ALL).call();
                List<Ref> headBranches = branches.stream()
                        .filter(ref -> ref.getName().startsWith(Constants.R_HEADS))
                        .collect(Collectors.toList());
                long totalBranches = headBranches.size();

                List<BranchInfo> branchInfos = new ArrayList<>();
                for (Ref branchRef : headBranches) {
                    try (RevWalk revWalk = new RevWalk(repository)) {
                        RevCommit latestBranchCommit = revWalk.parseCommit(branchRef.getObjectId());
                        branchInfos.add(BranchInfo.builder()
                                .name(repository.shortenRefName(branchRef.getName()))
                                .latestCommitHash(latestBranchCommit.getId().getName())
                                .latestCommitMessage(latestBranchCommit.getShortMessage())
                                .latestCommitAuthor(latestBranchCommit.getAuthorIdent().getName())
                                .latestCommitDate(Instant.ofEpochSecond(latestBranchCommit.getCommitTime()))
                                .build());
                    }
                }

                // Tags
                long totalTags = git.tagList().call().size();

                // Contributors
                Set<ContributorInfo> contributors = new HashSet<>();
                Map<String, Long> authorCommitCounts = new HashMap<>();

                for (RevCommit commit : commitList) {
                    String authorEmail = commit.getAuthorIdent().getEmailAddress();
                    String authorName = commit.getAuthorIdent().getName();
                    authorCommitCounts.merge(authorEmail, 1L, Long::sum);

                    // Add/update contributor info - ensuring uniqueness by email
                    // This approach is not ideal for `Set` as it removes and re-adds.
                    // A better way would be to compute all counts first then create ContributorInfo objects.
                    // However, keeping it as is from your original code for minimal disruption.
                    contributors.removeIf(c -> c.getEmail().equals(authorEmail));
                    contributors.add(ContributorInfo.builder()
                            .name(authorName)
                            .email(authorEmail)
                            .commitCount(authorCommitCounts.get(authorEmail))
                            .build());
                }

                // --- NEW: Daily Statistics Calculation for the Graph ---
                Map<LocalDate, Long> dailyNewCommits = new HashMap<>();
                Map<LocalDate, Long> dailyMergedCommits = new HashMap<>();

                // Determine the time window (last 7 days, including today)
                LocalDate today = LocalDate.now();
                // Use a specific ZoneId if your Git commits are in a different timezone than system default
                ZoneId defaultZoneId = ZoneId.systemDefault();

                // Initialize maps for the last 7 days with zero counts
                // This ensures all days (Mon-Sun like in the graph) are represented, even if no commits occurred.
                for (int i = 6; i >= 0; i--) { // Loop from 6 days ago up to today
                    LocalDate date = today.minusDays(i);
                    dailyNewCommits.put(date, 0L);
                    dailyMergedCommits.put(date, 0L);
                }


                // Iterate through all commits to populate daily stats
                for (RevCommit commit : commitList) {
                    LocalDate commitDate = Instant.ofEpochSecond(commit.getCommitTime())
                            .atZone(defaultZoneId)
                            .toLocalDate();

                    // Only process commits within the last 7 days (inclusive of today)
                    if (!commitDate.isBefore(today.minusDays(6)) && !commitDate.isAfter(today)) {
                        // Increment count for all commits made on this day ("New")
                        dailyNewCommits.merge(commitDate, 1L, Long::sum);

                        // If it's a merge commit (has more than one parent), increment "Closed" count
                        if (commit.getParentCount() > 1) {
                            dailyMergedCommits.merge(commitDate, 1L, Long::sum);
                        }
                    }
                }

                // Sort the maps by date. Although a Map doesn't guarantee order,
                // using LinkedHashMap for `Collectors.toMap` with sorting ensures
                // a predictable iteration order if needed for displaying.
                Map<LocalDate, Long> sortedDailyNewCommits = dailyNewCommits.entrySet().stream()
                        .sorted(Map.Entry.comparingByKey())
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (oldValue, newValue) -> oldValue, LinkedHashMap::new)); // Use LinkedHashMap to preserve order

                Map<LocalDate, Long> sortedDailyMergedCommits = dailyMergedCommits.entrySet().stream()
                        .sorted(Map.Entry.comparingByKey())
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (oldValue, newValue) -> oldValue, LinkedHashMap::new)); // Use LinkedHashMap to preserve order

                return RepositoryStatistics.builder()
                        .repositoryName(repoName)
                        .totalCommits(totalCommits)
                        .totalBranches(totalBranches)
                        .totalTags(totalTags)
                        .lastCommitDate(lastCommit != null ? Instant.ofEpochSecond(lastCommit.getCommitTime()) : null)
                        .lastCommitAuthor(lastCommit != null ? lastCommit.getAuthorIdent().getName() : null)
                        .contributors(contributors)
                        .branches(branchInfos)
                        // Add the newly calculated daily statistics
                        .dailyNewCommits(sortedDailyNewCommits)
                        .dailyMergedCommits(sortedDailyMergedCommits)
                        .build();

            } catch (GitAPIException | IOException e) {
                log.error("Error generating statistics for repository '{}': {}", repoName, e.getMessage(), e);
                return null;
            }
        }
    }


    /**
     * Lists commits for a given repository and optionally a specific branch.
     * @param repoName The name of the repository.
     * @param branchName Optional: The name of the branch (e.g., "main", "develop"). If null, lists all commits.
     * @param limit The maximum number of commits to return.
     * @return List of CommitInfo objects.
     */
    public List<CommitInfo> listCommits(String repoName, String branchName, int limit) {
        List<CommitInfo> commitInfos = new ArrayList<>();
        try (Repository repository = openJGitRepository(repoName).orElse(null)) {
            if (repository == null) {
                return Collections.emptyList();
            }

            try (Git git = new Git(repository)) {
                Iterable<RevCommit> commits;
                if (branchName != null && !branchName.isEmpty()) {
                    Ref branchRef = repository.findRef(Constants.R_HEADS + branchName);
                    if (branchRef == null) {
                        log.warn("Branch '{}' not found in repository '{}'.", branchName, repoName);
                        return Collections.emptyList();
                    }
                    commits = git.log().add(branchRef.getObjectId()).setMaxCount(limit).call();
                } else {
                    // If no branch specified, get all commits from all heads
                    commits = git.log().all().setMaxCount(limit).call();
                }

                for (RevCommit commit : commits) {
                    commitInfos.add(CommitInfo.builder()
                            .hash(commit.getId().getName())
                            .shortMessage(commit.getShortMessage())
                            .fullMessage(commit.getFullMessage())
                            .authorName(commit.getAuthorIdent().getName())
                            .authorEmail(commit.getAuthorIdent().getEmailAddress())
                            .committerName(commit.getCommitterIdent().getName())
                            .committerEmail(commit.getCommitterIdent().getEmailAddress())
                            .commitDate(Instant.ofEpochSecond(commit.getCommitTime()))
                            .build());
                }
            } catch (GitAPIException | IOException e) {
                log.error("Error listing commits for repository '{}', branch '{}': {}", repoName, branchName, e.getMessage(), e);
            }
        }
        return commitInfos;
    }

    /**
     * Lists all contributors for a given repository along with their commit counts.
     * @param repoName The name of the repository.
     * @return A Set of ContributorInfo objects.
     */
    public Set<ContributorInfo> listContributors(String repoName) {
        Set<ContributorInfo> contributors = new HashSet<>();
        Map<String, Long> authorCommitCounts = new HashMap<>();

        try (Repository repository = openJGitRepository(repoName).orElse(null)) {
            if (repository == null) {
                return Collections.emptySet();
            }

            try (Git git = new Git(repository)) {
                Iterable<RevCommit> allCommits = git.log().all().call();
                for (RevCommit commit : allCommits) {
                    String authorEmail = commit.getAuthorIdent().getEmailAddress();
                    String authorName = commit.getAuthorIdent().getName();
                    authorCommitCounts.merge(authorEmail, 1L, Long::sum);

                    // Add/update contributor info
                    contributors.removeIf(c -> c.getEmail().equals(authorEmail)); // Remove old entry if exists
                    contributors.add(ContributorInfo.builder()
                            .name(authorName)
                            .email(authorEmail)
                            .commitCount(authorCommitCounts.get(authorEmail)) // Get current count
                            .build());
                }
            } catch (GitAPIException | IOException e) {
                log.error("Error listing contributors for repository '{}': {}", repoName, e.getMessage(), e);
            }
        }
        return contributors;
    }

    /**
     * Lists all branches for a given repository.
     * @param repoName The name of the repository.
     * @return List of BranchInfo objects.
     */
    public List<BranchInfo> listBranches(String repoName) {
        List<BranchInfo> branchInfos = new ArrayList<>();
        try (Repository repository = openJGitRepository(repoName).orElse(null)) {
            if (repository == null) {
                return Collections.emptyList();
            }

            try (Git git = new Git(repository)) {
                List<Ref> branches = git.branchList().setListMode(org.eclipse.jgit.api.ListBranchCommand.ListMode.ALL).call();
                List<Ref> headBranches = branches.stream()
                        .filter(ref -> ref.getName().startsWith(Constants.R_HEADS))
                        .collect(Collectors.toList());

                for (Ref branchRef : headBranches) {
                    try (RevWalk revWalk = new RevWalk(repository)) {
                        RevCommit latestBranchCommit = revWalk.parseCommit(branchRef.getObjectId());
                        branchInfos.add(BranchInfo.builder()
                                .name(repository.shortenRefName(branchRef.getName()))
                                .latestCommitHash(latestBranchCommit.getId().getName())
                                .latestCommitMessage(latestBranchCommit.getShortMessage())
                                .latestCommitAuthor(latestBranchCommit.getAuthorIdent().getName())
                                .latestCommitDate(Instant.ofEpochSecond(latestBranchCommit.getCommitTime()))
                                .build());
                    }
                }
            } catch (GitAPIException | IOException e) {
                log.error("Error listing branches for repository '{}': {}", repoName, e.getMessage(), e);
            }
        }
        return branchInfos;
    }
}