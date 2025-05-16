package com.esprit.gitesprit.git; // Assuming this is your correct package

import jakarta.servlet.Servlet;
import jakarta.servlet.http.HttpServletRequest;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.http.server.GitServlet;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.ReceivePack;
import org.eclipse.jgit.transport.UploadPack; // Import if you set UploadPackFactory
import org.eclipse.jgit.transport.resolver.RepositoryResolver;
import org.eclipse.jgit.transport.resolver.ServiceNotAuthorizedException;
import org.eclipse.jgit.transport.resolver.ServiceNotEnabledException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class JGitServletConfig {

    private static final Logger log = LoggerFactory.getLogger(JGitServletConfig.class);

    @Value("${git.server.repos.path}")
    private String repositoriesBasePath;

    @Value("${git.server.repos.auto-create-on-push:true}") // Defaulting to true for this example
    private boolean autoCreateRepositoriesOnPush;

    @Bean
    public ServletRegistrationBean<Servlet> gitServletRegistrationBean() throws IOException {
        GitServlet gitServlet = new GitServlet();

        Path basePath = Paths.get(repositoriesBasePath);
        if (!Files.exists(basePath)) {
            Files.createDirectories(basePath);
            log.info("Created Git repositories base directory: {}", basePath.toAbsolutePath());
        } else {
            log.info("Using Git repositories base directory: {}", basePath.toAbsolutePath());
        }

        gitServlet.setRepositoryResolver(new CreateOnPushRepositoryResolver(
                repositoriesBasePath,
                autoCreateRepositoriesOnPush
        ));

        // --- Configure factories for UploadPack (fetch/clone) and ReceivePack (push) ---
        // This allows operations on any resolved repository.
        // Add authorization checks here if needed.

        gitServlet.setUploadPackFactory((req, db) -> {
            log.debug("Creating UploadPack for repository: {} for request URI: {}",
                    (db != null && db.getDirectory() != null ? db.getDirectory().getName() : "N/A"),
                    req.getRequestURI()
            );
            if (db == null) { // Should ideally not happen if resolver did its job
                throw new ServiceNotEnabledException("Repository not available for UploadPack operation.");
            }
            return new UploadPack(db);
        });

        gitServlet.setReceivePackFactory((req, db) -> {
            log.debug("Creating ReceivePack for repository: {} for request URI: {}",
                    (db != null && db.getDirectory() != null ? db.getDirectory().getName() : "N/A"),
                    req.getRequestURI()
            );
            if (db == null) { // Should ideally not happen
                throw new ServiceNotEnabledException("Repository not available for ReceivePack operation.");
            }
            // Potentially check db.getConfig().getBoolean("http", "receivepack", true/false)
            // But for now, this factory enables it by default.
            ReceivePack rp = new ReceivePack(db);
            // rp.setAllowPushOptions(true); // Optional: if you want to support git push --push-option
            // rp.setAllowDeletes(true);   // Optional: if you want to allow deleting remote branches
            // rp.setAllowNonFastForwards(true); // Optional: if you want to allow force pushes (use with caution)
            return rp;
        });
        // --- End of factory configuration ---

        ServletRegistrationBean<Servlet> registration =
                new ServletRegistrationBean<>(gitServlet, "/git/*");
        registration.setName("GitServlet");
        return registration;
    }

    static class CreateOnPushRepositoryResolver implements RepositoryResolver<HttpServletRequest> {
        private final File baseDir;
        private final boolean autoCreateOnPushEnabled; // Renamed for clarity
        private static final Logger resolverLog = LoggerFactory.getLogger(CreateOnPushRepositoryResolver.class);

        public CreateOnPushRepositoryResolver(String basePath, boolean autoCreateOnPush) {
            this.baseDir = new File(basePath);
            this.autoCreateOnPushEnabled = autoCreateOnPush;
            if (!this.baseDir.exists()) {
                try {
                    resolverLog.info("Base repository directory {} does not exist. Attempting to create.", this.baseDir.getAbsolutePath());
                    Files.createDirectories(this.baseDir.toPath());
                } catch (IOException e) {
                    resolverLog.error("Failed to create base repository directory: {}", this.baseDir.getAbsolutePath(), e);
                    // Consider throwing a runtime exception here to prevent app startup if base dir is critical
                }
            } else if (!this.baseDir.isDirectory()) {
                resolverLog.error("Base repository path {} exists but is not a directory.", this.baseDir.getAbsolutePath());
                // Consider throwing a runtime exception
            }
        }

        @Override
        public Repository open(HttpServletRequest req, String name)
                throws ServiceNotAuthorizedException, ServiceNotEnabledException {
            resolverLog.debug("Attempting to resolve repository: '{}', Request: {} {}?{}",
                    name, req.getMethod(), req.getRequestURI(), req.getQueryString());

            // Basic path sanitization
            if (name == null || name.trim().isEmpty() || name.contains("..") || name.contains("/") || name.contains("\\")) {
                resolverLog.warn("Invalid repository name requested: '{}'", name);
                throw new ServiceNotEnabledException("Invalid repository name: " + name);
            }

            String repoName = name;
            if (!repoName.endsWith(".git")) {
                repoName += ".git";
            }

            File repoDir = new File(baseDir, repoName);
            resolverLog.debug("Resolved repository path: {}", repoDir.getAbsolutePath());

            if (!repoDir.exists()) {
                String service = req.getParameter("service");
                boolean isPushRelatedOperation = "git-receive-pack".equals(service);

                // The actual push operation is a POST to /git/<repo>/git-receive-pack
                if (!isPushRelatedOperation && "POST".equalsIgnoreCase(req.getMethod()) &&
                        req.getRequestURI().endsWith("/git-receive-pack")) {
                    isPushRelatedOperation = true;
                }

                resolverLog.debug("Repository does not exist. Is push-related? {}. Auto-create enabled? {}",
                        isPushRelatedOperation, autoCreateOnPushEnabled);

                if (autoCreateOnPushEnabled && isPushRelatedOperation) {
                    resolverLog.info("Repository '{}' not found. Auto-creating due to push-related operation (service='{}', method='{}').",
                            repoName, service, req.getMethod());
                    try {
                        // The parent directory (baseDir) must exist.
                        if (!baseDir.exists() && !baseDir.mkdirs()) {
                            resolverLog.error("Failed to create base directory for repositories: {}", baseDir.getAbsolutePath());
                            throw new IOException("Failed to create base repository directory: " + baseDir.getAbsolutePath());
                        }
                        // Create a new bare repository using try-with-resources for the Git object
                        try (Git git = Git.init().setDirectory(repoDir).setBare(true).call()) {
                            resolverLog.info("Successfully auto-created bare repository: {}", repoDir.getAbsolutePath());
                        }
                    } catch (GitAPIException | IOException e) {
                        resolverLog.error("Failed to auto-create repository '{}': {}", repoName, e.getMessage(), e);
                        throw new ServiceNotEnabledException("Failed to create repository " + repoName, e);
                    }
                } else if (isPushRelatedOperation) {
                    resolverLog.warn("Push-related operation for non-existent repository '{}' denied (auto-create is disabled).", repoName);
                    throw new ServiceNotEnabledException("Repository not found and auto-creation is disabled: " + repoName);
                } else {
                    resolverLog.debug("Repository '{}' not found for non-push-related operation (service='{}').", repoName, service);
                    throw new ServiceNotEnabledException("Repository not found: " + repoName);
                }
            } else if (!repoDir.isDirectory()) {
                resolverLog.warn("Path {} exists but is not a directory.", repoDir.getAbsolutePath());
                throw new ServiceNotEnabledException("Repository path exists but is not a directory: " + repoName);
            }

            // At this point, repoDir should exist (either pre-existing or auto-created)
            // and be a directory. Now, verify it's a valid Git repository structure.
            if (!isValidGitRepository(repoDir)) {
                // This can happen if auto-creation failed silently or if an empty directory exists
                // with the repo name but isn't a git repo.
                resolverLog.error("Path {} exists but is not a valid (bare) Git repository.", repoDir.getAbsolutePath());
                throw new ServiceNotEnabledException("Not a valid Git repository: " + repoName);
            }

            try {
                resolverLog.debug("Attempting to build repository object for valid Git directory: {}", repoDir.getAbsolutePath());
                return new FileRepositoryBuilder()
                        .setGitDir(repoDir) // For bare repositories, repoDir IS the .git directory.
                        .readEnvironment()
                        .findGitDir() // Less critical if .setGitDir is used correctly for bare repos.
                        .setMustExist(true) // It must exist and be a valid git dir.
                        .build();
            } catch (IOException e) {
                resolverLog.error("Cannot open repository '{}' at {}: {}", repoName, repoDir.getAbsolutePath(), e.getMessage(), e);
                throw new ServiceNotEnabledException("Cannot open repository " + repoName, e);
            }
        }

        /**
         * Checks if the given directory appears to be a valid bare Git repository.
         */
        private boolean isValidGitRepository(File gitDir) {
            if (!gitDir.exists() || !gitDir.isDirectory()) {
                resolverLog.trace("isValidGitRepository: false, path does not exist or not a directory: {}", gitDir.getAbsolutePath());
                return false;
            }
            // Essential files/directories for a bare Git repository
            boolean hasHead = new File(gitDir, "HEAD").exists();
            boolean hasConfig = new File(gitDir, "config").isFile(); // config is a file
            boolean hasObjectsDir = new File(gitDir, "objects").isDirectory();
            boolean hasRefsDir = new File(gitDir, "refs").isDirectory();

            boolean isValid = hasHead && hasConfig && hasObjectsDir && hasRefsDir;
            resolverLog.trace("isValidGitRepository for {}: HEAD={}, config={}, objects={}, refs={} -> {}",
                    gitDir.getAbsolutePath(), hasHead, hasConfig, hasObjectsDir, hasRefsDir, isValid);
            return isValid;
        }
    }
}