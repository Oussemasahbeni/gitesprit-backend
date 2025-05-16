package com.esprit.gitesprit.git;

import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/repos")
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
            e.printStackTrace(); // Log this properly
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating repository: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<String>> listRepositories() {
        try {
            return ResponseEntity.ok(gitRepositoryService.listRepositories());
        } catch (IOException e) {
            e.printStackTrace(); // Log this properly
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Or an error object
        }
    }
}