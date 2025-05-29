package com.esprit.gitesprit.git.infrastructure.adapter.persistence;

import com.esprit.gitesprit.git.infrastructure.entity.GitRepositoryEntity;
import com.esprit.gitesprit.git.domain.model.GitRepository;
import com.esprit.gitesprit.git.domain.port.output.GitRepositories;
import com.esprit.gitesprit.git.infrastructure.mapper.GitRepositoryMapper;
import com.esprit.gitesprit.git.infrastructure.repository.GitRepositoryRepository;
import com.esprit.gitesprit.shared.annotation.PersistenceAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@PersistenceAdapter
@RequiredArgsConstructor
@Transactional
public class RepositoryJpaAdapter implements GitRepositories {
    
    private final GitRepositoryRepository gitRepositoryRepository;
    private final GitRepositoryMapper gitRepositoryMapper;
    
    @Override
    public GitRepository save(GitRepository gitRepository) {
        GitRepositoryEntity gitRepositoryEntity = gitRepositoryMapper.toEntity(gitRepository);
        GitRepositoryEntity savedAcademicEntity = gitRepositoryRepository.save(gitRepositoryEntity);
        return gitRepositoryMapper.toModel(savedAcademicEntity);
    }
}
