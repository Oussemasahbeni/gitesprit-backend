package com.esprit.gitesprit.git.domain.port.output;

import com.esprit.gitesprit.git.domain.model.GitRepository;

public interface GitRepositories {
    GitRepository save(GitRepository gitRepository);
}
