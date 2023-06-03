DROP TABLE IF EXISTS hits CASCADE;

CREATE TABLE IF NOT EXISTS hits
(
    id      BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    app     VARCHAR(20)                             NOT NULL,
    uri     VARCHAR(50)                             NOT NULL,
    ip      VARCHAR(20)                             NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    CONSTRAINT pk_hits PRIMARY KEY (id)
);