CREATE TABLE task_links
(
    task_id UUID NOT NULL,
    link    VARCHAR(255)
);

CREATE TABLE tasks
(
    id               UUID         NOT NULL,
    created_by       VARCHAR(255),
    last_modified_by VARCHAR(255),
    created_at       TIMESTAMP WITHOUT TIME ZONE,
    updated_at       TIMESTAMP WITHOUT TIME ZONE,
    version          SMALLINT     NOT NULL,
    description      VARCHAR(255) NOT NULL,
    due_date         date         NOT NULL,
    mark             DOUBLE PRECISION,
    comment          TEXT,
    done             BOOLEAN,
    group_student_id UUID         NOT NULL,
    CONSTRAINT pk_tasks PRIMARY KEY (id)
);

ALTER TABLE tasks
    ADD CONSTRAINT FK_TASKS_ON_GROUP_STUDENT FOREIGN KEY (group_student_id) REFERENCES group_students (id);

ALTER TABLE task_links
    ADD CONSTRAINT fk_task_links_on_task_entity FOREIGN KEY (task_id) REFERENCES tasks (id);