CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(255)                            NOT NULL,
    email VARCHAR(100)                            NOT NULL,
    CONSTRAINT PK_USER PRIMARY KEY (id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255)                            NOT NULL unique,
    CONSTRAINT pk_category PRIMARY KEY (id)
);


create table if not exists locations
(
    id  BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    lat FLOAT                                   not null,
    lon FLOAT                                   not null,
    CONSTRAINT pk_location PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS events
(
    event_id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title              VARCHAR(120)                            NOT NULL,
    annotation         VARCHAR(2000)                           NOT NULL,
    category_id        BIGINT,
    confirmed_requests BIGINT,
    created_on         TIMESTAMP WITHOUT TIME ZONE default current_timestamp,
    description        VARCHAR(7000)                           NOT NULL,
    event_date         TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    initiator_id       BIGINT,
    location_id        BIGINT,
    paid               BOOLEAN,
    participant_limit  INTEGER,
    published_on       TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN,
    state              VARCHAR(10)                             NOT NULL,
    views              BIGINT,
    CONSTRAINT pk_events PRIMARY KEY (event_id),
    CONSTRAINT FK_EVENTS_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT FK_EVENTS_ON_INITIATOR FOREIGN KEY (initiator_id) REFERENCES users (id),
    CONSTRAINT FK_EVENTS_ON_LOCATION FOREIGN KEY (location_id) REFERENCES locations (id)
);

create table if not exists compilations_title
(
    title_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title    VARCHAR(255)                            NOT NULL unique,
    pined    BOOLEAN,
    CONSTRAINT pk_compilations_title PRIMARY KEY (title_id)
);

create table if not exists compilations
(
    id       BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    event_id BIGINT,
    title_id BIGINT                                  NOT NULL,
    CONSTRAINT pk_compilations PRIMARY KEY (id),
    CONSTRAINT FK_COMPILATIONS_ON_EVENT FOREIGN KEY (event_id) REFERENCES events (event_id),
    CONSTRAINT FK_COMPILATIONS_ON_ID FOREIGN KEY (title_id) REFERENCES compilations_title (title_id)
);

CREATE TABLE IF NOT EXISTS participations
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created      TIMESTAMP WITHOUT TIME ZONE,
    event_id     BIGINT,
    requester_id BIGINT UNIQUE,
    status       VARCHAR(10)                             NOT NULL,
    CONSTRAINT pk_participations PRIMARY KEY (id),
    CONSTRAINT FK_PARTICIPATIONS_ON_EVENT FOREIGN KEY (event_id) REFERENCES events (event_id),
    CONSTRAINT FK_PARTICIPATIONS_ON_REQUESTER FOREIGN KEY (requester_id) REFERENCES users (id)
);
