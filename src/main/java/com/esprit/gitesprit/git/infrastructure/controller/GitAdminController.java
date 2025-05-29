package com.esprit.gitesprit.git.infrastructure.controller;

import com.esprit.gitesprit.git.domain.service.GitRepositoryService;
import com.esprit.gitesprit.git.domain.enums.ContentType;
import com.esprit.gitesprit.git.domain.model.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/repos")
@RequiredArgsConstructor
public class GitAdminController {

    private final GitRepositoryService gitRepositoryService;

    @PostMapping("/{repoName}")
    public ResponseEntity<String> createRepository(@PathVariable String repoName) {
        try {
            // Basic validation for repository name
            if (repoName == null || repoName.trim().isEmpty() || !repoName.matches("^[a-zA-Z0-9_-]+$")) {
                return ResponseEntity.badRequest().body("Invalid repository name. Use A-Z, a-z, 0-9, _, -.");
            }

            boolean created = gitRepositoryService.createRepository(repoName);
            if (created) {
                return ResponseEntity.status(HttpStatus.CREATED).body("Repository '" + repoName + ".git' created successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Repository '" + repoName + ".git' already exists.");
            }
        } catch (IOException | GitAPIException e) {
            e.printStackTrace(); // Log this properly using a logger (e.g., Slf4j)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating repository: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<String>> listRepositories() {
        try {
            return ResponseEntity.ok(gitRepositoryService.listRepositories());
        } catch (Exception e) { // Catch generic exception as listRepositories no longer throws IOException
            e.printStackTrace(); // Log this properly
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Or an error object
        }
    }

    @GetMapping("/{repoName}/statistics")
    public ResponseEntity<RepositoryStatistics> getRepositoryStatistics(@PathVariable String repoName) {
        RepositoryStatistics stats = gitRepositoryService.getRepositoryStatistics(repoName);
        if (stats != null) {
            return ResponseEntity.ok(stats);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{repoName}/commits")
    public ResponseEntity<List<CommitInfo>> listCommits(
            @PathVariable String repoName,
            @RequestParam(required = false) String branch,
            @RequestParam(defaultValue = "100") int limit) {
        List<CommitInfo> commits = gitRepositoryService.listCommits(repoName, branch, limit);
        if (commits != null) {
            return ResponseEntity.ok(commits);
        } else {
            return ResponseEntity.ok(Collections.emptyList()); // Or 404 if repo not found
        }
    }

    @GetMapping("/{repoName}/contributors")
    public ResponseEntity<Set<ContributorInfo>> listContributors(@PathVariable String repoName) {
        Set<ContributorInfo> contributors = gitRepositoryService.listContributors(repoName);
        if (contributors != null) {
            return ResponseEntity.ok(contributors);
        } else {
            return ResponseEntity.ok(Collections.emptySet()); // Or 404 if repo not found
        }
    }

    @GetMapping("/{repoName}/branches")
    public ResponseEntity<List<BranchInfo>> listBranches(@PathVariable String repoName) {
        List<BranchInfo> branches = gitRepositoryService.listBranches(repoName);
        if (branches != null) {
            return ResponseEntity.ok(branches);
        } else {
            return ResponseEntity.ok(Collections.emptyList()); // Or 404 if repo not found
        }
    }

    @GetMapping("/{repoName}/contents")
    public ResponseEntity<List<RepositoryContent>> getRepositoryContents(
            @PathVariable String repoName,
            @RequestParam String branchName) {

        List<RepositoryContent> contents = gitRepositoryService.getBranchContents(repoName, branchName);

        if (contents.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(contents);
    }

    // You might also want an endpoint to download a single file
    @GetMapping("/{repoName}/file/{branchName}/**") // Using /** to capture the full path
    public ResponseEntity<byte[]> downloadFile(
            @PathVariable String repoName,
            @PathVariable String branchName,
            HttpServletRequest request) { // Inject HttpServletRequest to get the full path

        String fullPath = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String filePath = fullPath.substring(fullPath.indexOf(branchName) + branchName.length() + 1); // Extract path after branchName

        List<RepositoryContent> contents = gitRepositoryService.getBranchContents(repoName, branchName);
        Optional<RepositoryContent> fileContent = contents.stream()
                .filter(content -> content.getType() == ContentType.FILE && content.getPath().equals(filePath))
                .findFirst();

        if (fileContent.isPresent()) {
            RepositoryContent file = fileContent.get();
            // You might need to set content type based on file extension
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .body(file.getContent());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}