CREATE TABLE log_entries
(
    id               UUID         NOT NULL,
    created_by       VARCHAR(255),
    last_modified_by VARCHAR(255),
    created_at       TIMESTAMP WITHOUT TIME ZONE,
    updated_at       TIMESTAMP WITHOUT TIME ZONE,
    version          SMALLINT     NOT NULL,
    user_id          UUID,
    action           VARCHAR(255) NOT NULL,
    device_id        VARCHAR(255),
    ip_address       VARCHAR(255) NOT NULL,
    module           VARCHAR(255) NOT NULL,
    message          TEXT,
    CONSTRAINT pk_log_entries PRIMARY KEY (id)
);

CREATE TABLE notifications
(
    id               UUID         NOT NULL,
    created_by       VARCHAR(255),
    last_modified_by VARCHAR(255),
    created_at       TIMESTAMP WITHOUT TIME ZONE,
    updated_at       TIMESTAMP WITHOUT TIME ZONE,
    version          SMALLINT     NOT NULL,
    type             VARCHAR(255) NOT NULL,
    data             JSONB,
    user_id          UUID         NOT NULL,
    is_read          BOOLEAN      NOT NULL,
    CONSTRAINT pk_notifications PRIMARY KEY (id)
);

CREATE TABLE users
(
    id               UUID         NOT NULL,
    created_by       VARCHAR(255),
    last_modified_by VARCHAR(255),
    created_at       TIMESTAMP WITHOUT TIME ZONE,
    updated_at       TIMESTAMP WITHOUT TIME ZONE,
    version          SMALLINT     NOT NULL,
    first_name       VARCHAR(255) NOT NULL,
    last_name        VARCHAR(255) NOT NULL,
    email            VARCHAR(255) NOT NULL,
    username         VARCHAR(255) NOT NULL,
    phone_number     VARCHAR(255),
    profile_picture  VARCHAR(255),
    locale           VARCHAR(255),
    birth_date       date,
    address          VARCHAR(255),
    gender           VARCHAR(255),
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE users
    ADD CONSTRAINT uc_users_username UNIQUE (username);

CREATE INDEX idx_action ON log_entries (action);

CREATE INDEX idx_is_read ON notifications (is_read);

CREATE INDEX idx_module ON log_entries (module);

CREATE INDEX idx_user_id_on_notifications ON notifications (user_id);

ALTER TABLE log_entries
    ADD CONSTRAINT FK_LOG_ENTRIES_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

CREATE INDEX idx_user ON log_entries (user_id);