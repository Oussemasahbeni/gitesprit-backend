package com.esprit.gitesprit.git.infrastructure.mapper;

import com.esprit.gitesprit.git.domain.model.GitRepository;
import com.esprit.gitesprit.git.infrastructure.dto.RepositoryDto;
import com.esprit.gitesprit.git.infrastructure.entity.GitRepositoryEntity;
import com.esprit.gitesprit.shared.mapstruct.CycleAvoidingMappingContext;
import com.esprit.gitesprit.shared.mapstruct.DoIgnore;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface GitRepositoryMapper {
    GitRepositoryEntity mapToEntity(GitRepository gitRepository, @Context CycleAvoidingMappingContext context);

    GitRepository mapToModel(GitRepositoryEntity gitRepositoryEntity, @Context CycleAvoidingMappingContext context);

    @DoIgnore
    default GitRepositoryEntity toEntity(GitRepository gitRepository) {
        return mapToEntity(gitRepository, new CycleAvoidingMappingContext());
    }

    @DoIgnore
    default GitRepository toModel(GitRepositoryEntity gitRepositoryEntity) {
        return mapToModel(gitRepositoryEntity, new CycleAvoidingMappingContext());
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    GitRepositoryEntity partialUpdate(GitRepositoryEntity gitRepository, @MappingTarget GitRepositoryEntity target);

//    GitRepository toModelFromDto(AddGitRepositoryDto gitRepositoryDto);
//
//    //GitRepository toModelFromUpdateDto( gitRepositoryDto);
//
    RepositoryDto toResponseDto(GitRepository gitRepository);
//
//    GitRepositorySimpleDto toSimpleDto(GitRepository gitRepository);
}
