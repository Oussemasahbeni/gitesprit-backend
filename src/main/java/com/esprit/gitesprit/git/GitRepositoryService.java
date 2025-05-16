package com.esprit.gitesprit.git;


import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class GitRepositoryService {

    @Value("${git.server.repos.path}")
    private String repositoriesBasePath;

    public boolean createRepository(String repoName) throws IOException, GitAPIException {
        // Ensure repoName ends with .git for convention
        if (!repoName.endsWith(".git")) {
            repoName += ".git";
        }

        File repoDir = new File(repositoriesBasePath, repoName);

        if (repoDir.exists()) {
            // Repository already exists
            return false;
        }

        // Create a new bare repository
        Git.init().setDirectory(repoDir).setBare(true).call();
        System.out.println("Created new bare repository: " + repoDir.getAbsolutePath());
        return true;
    }

    public List<String> listRepositories() throws IOException {
        Path basePath = Paths.get(repositoriesBasePath);
        if (!Files.exists(basePath) || !Files.isDirectory(basePath)) {
            return List.of();
        }
        try (Stream<Path> stream = Files.list(basePath)) {
            return stream
                    .filter(Files::isDirectory)
                    .filter(path -> path.getFileName().toString().endsWith(".git")) // List only .git dirs
                    .map(path -> path.getFileName().toString())
                    .collect(Collectors.toList());
        }
    }

    public Path getRepositoryPath(String repoName) {
        if (!repoName.endsWith(".git")) {
            repoName += ".git";
        }
        return Paths.get(repositoriesBasePath, repoName);
    }
}
