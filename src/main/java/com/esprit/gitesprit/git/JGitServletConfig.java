package com.esprit.gitesprit.git;

import com.esprit.gitesprit.git.infrastructure.entity.GitRepositoryEntity;
import com.esprit.gitesprit.git.infrastructure.repository.GitRepositoryRepository;
import jakarta.servlet.Servlet;
import jakarta.servlet.http.HttpServletRequest;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.http.server.GitServlet;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.ReceivePack;
import org.eclipse.jgit.transport.UploadPack;
import org.eclipse.jgit.transport.resolver.RepositoryResolver;
import org.eclipse.jgit.transport.resolver.ServiceNotAuthorizedException;
import org.eclipse.jgit.transport.resolver.ServiceNotEnabledException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional; // Import for transaction on auto-create

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Configuration
public class JGitServletConfig {

    private static final Logger log = LoggerFactory.getLogger(JGitServletConfig.class);

    @Value("${git.server.repos.path}")
    private String repositoriesBasePath;

    @Value("${git.server.repos.auto-create-on-push:true}")
    private boolean autoCreateRepositoriesOnPush;

    // Inject the GitRepositoryRepository here
    private final GitRepositoryRepository gitRepositoryRepository;

    public JGitServletConfig(GitRepositoryRepository gitRepositoryRepository) {
        this.gitRepositoryRepository = gitRepositoryRepository;
    }

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

        // Pass the GitRepositoryRepository to the custom resolver
        gitServlet.setRepositoryResolver(new CreateOnPushRepositoryResolver(
                repositoriesBasePath,
                autoCreateRepositoriesOnPush,
                gitRepositoryRepository // Pass the repository bean
        ));

        gitServlet.setUploadPackFactory((req, db) -> {
            log.debug("Creating UploadPack for repository: {} for request URI: {}",
                    (db != null && db.getDirectory() != null ? db.getDirectory().getName() : "N/A"),
                    req.getRequestURI()
            );
            if (db == null) {
                throw new ServiceNotEnabledException("Repository not available for UploadPack operation.");
            }
            return new UploadPack(db);
        });

        gitServlet.setReceivePackFactory((req, db) -> {
            log.debug("Creating ReceivePack for repository: {} for request URI: {}",
                    (db != null && db.getDirectory() != null ? db.getDirectory().getName() : "N/A"),
                    req.getRequestURI()
            );
            if (db == null) {
                throw new ServiceNotEnabledException("Repository not available for ReceivePack operation.");
            }
            ReceivePack rp = new ReceivePack(db);
            // rp.setAllowPushOptions(true);
            // rp.setAllowDeletes(true);
            // rp.setAllowNonFastForwards(true);
            return rp;
        });

        ServletRegistrationBean<Servlet> registration =
                new ServletRegistrationBean<>(gitServlet, "/git/*");
        registration.setName("GitServlet");
        return registration;
    }

    // Make this class public static to allow Spring to potentially autowire it,
    // though we are manually instantiating it and passing dependencies.
    // @Component // You could make it a component if it were not nested and needed wider use.
    static class CreateOnPushRepositoryResolver implements RepositoryResolver<HttpServletRequest> {
        private final File baseDir;
        private final boolean autoCreateOnPushEnabled;
        private final GitRepositoryRepository gitRepositoryRepository; // New: Inject repository
        private static final Logger resolverLog = LoggerFactory.getLogger(CreateOnPushRepositoryResolver.class);

        public CreateOnPushRepositoryResolver(String basePath, boolean autoCreateOnPush, GitRepositoryRepository gitRepositoryRepository) {
            this.baseDir = new File(basePath);
            this.autoCreateOnPushEnabled = autoCreateOnPush;
            this.gitRepositoryRepository = gitRepositoryRepository; // Assign injected repository
            if (!this.baseDir.exists()) {
                try {
                    resolverLog.info("Base repository directory {} does not exist. Attempting to create.", this.baseDir.getAbsolutePath());
                    Files.createDirectories(this.baseDir.toPath());
                } catch (IOException e) {
                    resolverLog.error("Failed to create base repository directory: {}", this.baseDir.getAbsolutePath(), e);
                    // Critical failure, might want to throw RuntimeException
                }
            } else if (!this.baseDir.isDirectory()) {
                resolverLog.error("Base repository path {} exists but is not a directory.", this.baseDir.getAbsolutePath());
                // Critical failure, might want to throw RuntimeException
            }
        }

        @Override
        @Transactional // Ensure DB operations within this method are transactional
        public Repository open(HttpServletRequest req, String name)
                throws ServiceNotAuthorizedException, ServiceNotEnabledException {
            resolverLog.debug("Attempting to resolve repository: '{}', Request: {} {}?{}",
                    name, req.getMethod(), req.getRequestURI(), req.getQueryString());

            if (name == null || name.trim().isEmpty() || name.contains("..") || name.contains("/") || name.contains("\\")) {
                resolverLog.warn("Invalid repository name requested: '{}'", name);
                throw new ServiceNotEnabledException("Invalid repository name: " + name);
            }

            String repoName = name;
            if (!repoName.endsWith(".git")) {
                repoName += ".git";
            }

            File repoDir = null; // Will be set either from DB or new creation

            // 1. Check if the repository exists in the database first
            Optional<GitRepositoryEntity> dbRepo = gitRepositoryRepository.findByRepositoryName(repoName);

            if (dbRepo.isPresent()) {
                resolverLog.debug("Repository '{}' found in database. Path: {}", repoName, dbRepo.get().getRepositoryPath());
                repoDir = new File(dbRepo.get().getRepositoryPath());

                if (!repoDir.exists()) {
                    resolverLog.warn("Inconsistency: Repository '{}' found in DB but not on filesystem at {}. Removing from DB.",
                            repoName, repoDir.getAbsolutePath());
                    // Remove from DB if filesystem copy is gone. This assumes filesystem is the ultimate source of truth for existence.
                    gitRepositoryRepository.delete(dbRepo.get());
                    throw new ServiceNotEnabledException("Repository not found on filesystem, removed from DB: " + repoName);
                }
            } else {
                // Repository not found in the database
                String service = req.getParameter("service");
                boolean isPushRelatedOperation = "git-receive-pack".equals(service);

                if (!isPushRelatedOperation && "POST".equalsIgnoreCase(req.getMethod()) &&
                        req.getRequestURI().endsWith("/git-receive-pack")) {
                    isPushRelatedOperation = true;
                }

                resolverLog.debug("Repository '{}' not found in DB. Is push-related? {}. Auto-create enabled? {}",
                        repoName, isPushRelatedOperation, autoCreateOnPushEnabled);

                if (autoCreateOnPushEnabled && isPushRelatedOperation) {
                    repoDir = new File(baseDir, repoName); // Define path for new repository
                    resolverLog.info("Repository '{}' not found. Auto-creating due to push-related operation (service='{}', method='{}') at {}",
                            repoName, service, req.getMethod(), repoDir.getAbsolutePath());
                    try {
                        if (!baseDir.exists() && !baseDir.mkdirs()) {
                            resolverLog.error("Failed to create base directory for repositories: {}", baseDir.getAbsolutePath());
                            throw new IOException("Failed to create base repository directory: " + baseDir.getAbsolutePath());
                        }

                        // Ensure filesystem doesn't already have it (race condition safety or prior failed creation)
                        if (repoDir.exists()) {
                            resolverLog.warn("Repository directory {} already exists on filesystem but not in DB during auto-create attempt. Adding to DB.", repoDir.getAbsolutePath());
                            // If it exists on filesystem but not in DB, and auto-create was triggered, add it to DB.
                            // This handles cases where a previous auto-create created the directory but failed to save to DB.
                            if (!isValidGitRepository(repoDir)) {
                                throw new ServiceNotEnabledException("Directory exists but is not a valid Git repository: " + repoDir.getAbsolutePath());
                            }
                        } else {
                            // Create a new bare repository on filesystem
                            try (Git git = Git.init().setDirectory(repoDir).setBare(true).call()) {
                                resolverLog.info("Successfully auto-created bare repository on filesystem: {}", repoDir.getAbsolutePath());
                            }
                        }

                        // Save to database only after successful filesystem creation/discovery
                        GitRepositoryEntity newRepo = new GitRepositoryEntity();
                        newRepo.setRepositoryName(repoName);
                        newRepo.setRepositoryPath(repoDir.getAbsolutePath());
                        gitRepositoryRepository.save(newRepo);
                        resolverLog.info("Saved auto-created repository to database: {}", repoName);

                    } catch (GitAPIException | IOException e) {
                        resolverLog.error("Failed to auto-create repository '{}': {}", repoName, e.getMessage(), e);
                        throw new ServiceNotEnabledException("Failed to create repository " + repoName, e);
                    }
                } else {
                    resolverLog.debug("Repository '{}' not found in DB for non-push-related operation (service='{}') or auto-create disabled.", repoName, service);
                    throw new ServiceNotEnabledException("Repository not found and auto-creation not permitted: " + repoName);
                }
            }

            // At this point, repoDir should be set and point to an existing directory
            // (either retrieved from DB, or newly auto-created).
            if (repoDir == null || !repoDir.isDirectory()) {
                resolverLog.error("Resolved path {} is not a valid directory for repository {}.",
                        repoDir != null ? repoDir.getAbsolutePath() : "null", repoName);
                throw new ServiceNotEnabledException("Invalid repository path or directory: " + repoName);
            }

            // Verify it's a valid Git repository structure, even if it came from DB
            if (!isValidGitRepository(repoDir)) {
                resolverLog.error("Path {} exists but is not a valid (bare) Git repository.", repoDir.getAbsolutePath());
                throw new ServiceNotEnabledException("Not a valid Git repository: " + repoName);
            }

            try {
                resolverLog.debug("Attempting to build repository object for valid Git directory: {}", repoDir.getAbsolutePath());
                return new FileRepositoryBuilder()
                        .setGitDir(repoDir)
                        .readEnvironment()
                        .findGitDir()
                        .setMustExist(true)
                        .build();
            } catch (IOException e) {
                resolverLog.error("Cannot open repository '{}' at {}: {}", repoName, repoDir.getAbsolutePath(), e.getMessage(), e);
                throw new ServiceNotEnabledException("Cannot open repository " + repoName, e);
            }
        }

        private boolean isValidGitRepository(File gitDir) {
            if (!gitDir.exists() || !gitDir.isDirectory()) {
                resolverLog.trace("isValidGitRepository: false, path does not exist or not a directory: {}", gitDir.getAbsolutePath());
                return false;
            }
            boolean hasHead = new File(gitDir, "HEAD").exists();
            boolean hasConfig = new File(gitDir, "config").isFile();
            boolean hasObjectsDir = new File(gitDir, "objects").isDirectory();
            boolean hasRefsDir = new File(gitDir, "refs").isDirectory();

            boolean isValid = hasHead && hasConfig && hasObjectsDir && hasRefsDir;
            resolverLog.trace("isValidGitRepository for {}: HEAD={}, config={}, objects={}, refs={} -> {}",
                    gitDir.getAbsolutePath(), hasHead, hasConfig, hasObjectsDir, hasRefsDir, isValid);
            return isValid;
        }
    }
}