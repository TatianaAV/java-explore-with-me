create table if not exists apps
(
    id   BIGINT generated by default as identity not null,
    name VARCHAR(255)                            not null unique,
    CONSTRAINT pk_app PRIMARY KEY (id)
);

create table if not exists endpoints
(
    id     bigint generated by default as identity not null,
    app_id bigint                                  not null,
    uri    varchar(255)                            not null,
    ip     varchar(39)                             not null,
    stamp  timestamp default current_timestamp,
    CONSTRAINT pk_endpoint PRIMARY KEY (id),
    CONSTRAINT fk_app FOREIGN KEY (app_id) REFERENCES apps (id)
);

