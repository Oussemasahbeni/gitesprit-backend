CREATE TABLE git_repository
(
    id               UUID         NOT NULL,
    created_by       VARCHAR(255),
    last_modified_by VARCHAR(255),
    created_at       TIMESTAMP WITHOUT TIME ZONE,
    updated_at       TIMESTAMP WITHOUT TIME ZONE,
    version          SMALLINT     NOT NULL,
    repository_name  VARCHAR(255) NOT NULL,
    repository_path  VARCHAR(255) NOT NULL,
    CONSTRAINT pk_git_repository PRIMARY KEY (id)
);

ALTER TABLE git_repository
    ADD CONSTRAINT uc_git_repository_repository_name UNIQUE (repository_name);