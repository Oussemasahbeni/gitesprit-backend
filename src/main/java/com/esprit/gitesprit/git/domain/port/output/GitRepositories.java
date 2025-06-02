package com.esprit.gitesprit.git.domain.port.output;

import com.esprit.gitesprit.git.domain.model.GitRepository;

import java.util.List;
import java.util.UUID;

public interface GitRepositories {
    GitRepository save(GitRepository gitRepository);

    List<GitRepository> findAllByGroupId(UUID groupID);
}
