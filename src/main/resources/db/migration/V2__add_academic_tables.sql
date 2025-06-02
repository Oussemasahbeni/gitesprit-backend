CREATE TABLE academic_years
(
    id               UUID     NOT NULL,
    created_by       VARCHAR(255),
    last_modified_by VARCHAR(255),
    created_at       TIMESTAMP WITHOUT TIME ZONE,
    updated_at       TIMESTAMP WITHOUT TIME ZONE,
    version          SMALLINT NOT NULL,
    start_year       INTEGER  NOT NULL,
    end_year         INTEGER  NOT NULL,
    CONSTRAINT pk_academic_years PRIMARY KEY (id)
);

CREATE TABLE classes
(
    id               UUID         NOT NULL,
    created_by       VARCHAR(255),
    last_modified_by VARCHAR(255),
    created_at       TIMESTAMP WITHOUT TIME ZONE,
    updated_at       TIMESTAMP WITHOUT TIME ZONE,
    version          SMALLINT     NOT NULL,
    name             VARCHAR(255) NOT NULL,
    academic_year_id UUID         NOT NULL,
    CONSTRAINT pk_classes PRIMARY KEY (id)
);



CREATE TABLE groups
(
    id               UUID         NOT NULL,
    created_by       VARCHAR(255),
    last_modified_by VARCHAR(255),
    created_at       TIMESTAMP WITHOUT TIME ZONE,
    updated_at       TIMESTAMP WITHOUT TIME ZONE,
    version          SMALLINT     NOT NULL,
    name             VARCHAR(255) NOT NULL,
    subject_id       UUID         NOT NULL,
    marks INTEGER DEFAULT 0,
    comments TEXT,
    CONSTRAINT pk_groups PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS group_students (
                                              id UUID PRIMARY KEY,
                                              created_by       VARCHAR(255),
    last_modified_by VARCHAR(255),
    created_at       TIMESTAMP WITHOUT TIME ZONE,
    updated_at       TIMESTAMP WITHOUT TIME ZONE,
    version          SMALLINT     NOT NULL,
    student_id UUID NOT NULL,
    group_id UUID NOT NULL,
    individual_mark DOUBLE PRECISION,
    individual_comment TEXT,
    CONSTRAINT uk_student_group UNIQUE (student_id, group_id),
    CONSTRAINT fk_student_group_marks_student FOREIGN KEY (student_id) REFERENCES users(id),
    CONSTRAINT fk_student_group_marks_group FOREIGN KEY (group_id) REFERENCES groups(id)
);


CREATE TABLE subjects
(
    id               UUID         NOT NULL,
    created_by       VARCHAR(255),
    last_modified_by VARCHAR(255),
    created_at       TIMESTAMP WITHOUT TIME ZONE,
    updated_at       TIMESTAMP WITHOUT TIME ZONE,
    version          SMALLINT     NOT NULL,
    name             VARCHAR(255) NOT NULL,
    classroom_id     UUID,
    teacher_id       UUID,
    group_mark_percentage DECIMAL(5,2)  NOT NULL,
    individual_mark_percentage DECIMAL(5,2)  NOT NULL,
    CONSTRAINT pk_subjects PRIMARY KEY (id)
);

ALTER TABLE classes
    ADD CONSTRAINT FK_CLASSES_ON_ACADEMIC_YEAR FOREIGN KEY (academic_year_id) REFERENCES academic_years (id);

ALTER TABLE groups
    ADD CONSTRAINT FK_GROUPS_ON_SUBJECT FOREIGN KEY (subject_id) REFERENCES subjects (id);

ALTER TABLE subjects
    ADD CONSTRAINT FK_SUBJECTS_ON_CLASSROOM FOREIGN KEY (classroom_id) REFERENCES classes (id);

ALTER TABLE subjects
    ADD CONSTRAINT FK_SUBJECTS_ON_TEACHER FOREIGN KEY (teacher_id) REFERENCES users (id);

ALTER TABLE group_students
    ADD CONSTRAINT fk_grostu_on_group_entity FOREIGN KEY (group_id) REFERENCES groups (id);

ALTER TABLE group_students
    ADD CONSTRAINT fk_grostu_on_user_entity FOREIGN KEY (student_id) REFERENCES users (id);