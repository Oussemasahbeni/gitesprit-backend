CREATE TABLE tasks
(
    id               UUID             NOT NULL,
    created_by       VARCHAR(255),
    last_modified_by VARCHAR(255),
    created_at       TIMESTAMP WITHOUT TIME ZONE,
    updated_at       TIMESTAMP WITHOUT TIME ZONE,
    version          SMALLINT         NOT NULL,
    description      VARCHAR(255)     NOT NULL,
    due_date         date             NOT NULL,
    mark             DOUBLE PRECISION,
    comment          TEXT,
    done             BOOLEAN,
    branch_link      VARCHAR(255),
    group_student_id UUID             NOT NULL,
    CONSTRAINT pk_tasks PRIMARY KEY (id)
);

ALTER TABLE tasks
    ADD CONSTRAINT FK_TASKS_ON_GROUP_STUDENT FOREIGN KEY (group_student_id) REFERENCES group_students (id);