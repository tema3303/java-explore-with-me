DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS locations CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS compilation_events CASCADE;
DROP TABLE IF EXISTS participation_request CASCADE;
DROP TABLE IF EXISTS rating CASCADE;

CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    email VARCHAR(254)                            NOT NULL,
    name  VARCHAR(250)                            NOT NULL,
    rate  INTEGER                                 NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uc_users_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(50)                             NOT NULL,
    CONSTRAINT pk_categories PRIMARY KEY (id),
    CONSTRAINT uc_categories_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS locations
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    latitude  FLOAT                                   NOT NULL,
    longitude FLOAT                                   NOT NULL,
    CONSTRAINT pk_locations PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilations
(
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title  VARCHAR(50)                             NOT NULL,
    pinned BOOLEAN                                 NOT NULL,

    CONSTRAINT pk_compilations PRIMARY KEY (id),
    CONSTRAINT uc_compilations_title UNIQUE (title)
);

CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    annotation         VARCHAR(2000)                           NOT NULL,
    category_id        BIGINT                                  NOT NULL,
    confirmed_requests INTEGER                                 NOT NULL,
    created_on         TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    description        VARCHAR(7000)                           NOT NULL,
    event_date         TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    initiator_id       BIGINT                                  NOT NULL,
    location_lat       FLOAT                                   NOT NULL,
    location_lon       FLOAT                                   NOT NULL,
    paid               BOOLEAN                                 NOT NULL,
    participant_limit  INTEGER                                 NOT NULL,
    published_on       TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN                                 NOT NULL,
    state              VARCHAR(10)                             NOT NULL,
    title              VARCHAR(120)                            NOT NULL,
    views              BIGINT,
    likes              INTEGER                                 NOT NULL,
    dislikes           INTEGER                                 NOT NULL,
    rate               INTEGER                                 NOT NULL,
    CONSTRAINT pk_events PRIMARY KEY (id),
    CONSTRAINT uc_events_annotation UNIQUE (annotation),
    CONSTRAINT fk_events_on_category FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT fk_events_on_initiator FOREIGN KEY (initiator_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS compilation_events
(
    compilation_id BIGINT NOT NULL,
    event_id       BIGINT NOT NULL,

    CONSTRAINT pk_compilation_events PRIMARY KEY (compilation_id, event_id),
    CONSTRAINT fk_compilation_events_on_compilations FOREIGN KEY (compilation_id) REFERENCES compilations (id) ON DELETE CASCADE,
    CONSTRAINT fk_compilation_events_on_events FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS participation_request
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created   TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    event_id     BIGINT                                  NOT NULL,
    requester_id BIGINT                                  NOT NULL,
    status    VARCHAR(50),
    CONSTRAINT pk_participation_request PRIMARY KEY (id),
    CONSTRAINT fk_participation_request_to_events FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE,
    CONSTRAINT fk_participation_request_to_users FOREIGN KEY (requester_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS rating
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    user_id     BIGINT                                  NOT NULL,
    event_id    BIGINT                                  NOT NULL,
    is_positive BOOLEAN                                 NOT NULL,

    CONSTRAINT pk_rating PRIMARY KEY (id),
    CONSTRAINT uc_rating_event_is_positive UNIQUE (user_id, event_id, is_positive)
);