create table regions
(
    id     uuid DEFAULT uuid_generate_v4(),
    chunkX INT,
    chunkZ INT,
    world  VARCHAR,
    PRIMARY KEY (id)
);
insert into regions (chunkX, chunkZ, world) VALUES (1, 1, 'world');
create table enclaves
(
    id    uuid DEFAULT uuid_generate_v4(),
    name  varchar(16) NOT NULL,
    color varchar(7)  NOT NULL,
    PRIMARY KEY (id)
);
create table claims
(
    id        uuid DEFAULT uuid_generate_v4(),
    date    date DEFAULT now(),
    regionId  uuid,
    enclaveId uuid,
    PRIMARY KEY (id),
    FOREIGN KEY (regionId) REFERENCES regions (id),
    FOREIGN KEY (enclaveId) REFERENCES enclaves (id)
);
create table membership
(
    uuid       uuid DEFAULT uuid_generate_v4(),
    playerUUID uuid,
    enclaveId  uuid,
    PRIMARY KEY (uuid),
    FOREIGN KEY (enclaveId) REFERENCES enclaves (id)
);


