CREATE TABLE role (
    id serial primary key, 
    name character varying(32) UNIQUE not null default '',
    comments character varying(256) not null default ''
);
CREATE INDEX role_1 on role (name);

CREATE TABLE grp (
    id serial NOT NULL,
    label character varying(32) UNIQUE NOT NULL,
    name character varying(64) UNIQUE NOT NULL null default '',
    comments character varying(256) not null default '',
    contact_uid int,
    admin_uid int
);
CREATE INDEX grp_idx1 ON grp (label);
CREATE INDEX grp_idx2 ON grp (name);

CREATE TABLE usr_role (
    usr_id integer not null default 0, 
    role_id integer not null default 0,
    primary key (usr_id,role_id) 
);
CREATE INDEX usr_role_1 on usr_role (role_id);
CREATE INDEX usr_role_2 on usr_role (role_id, usr_id);
CREATE INDEX usr_role_3 on usr_role (usr_id,role_id);

CREATE TABLE usr_grp (
    usr_id integer not null default 0, 
    grp_id integer not null default 0,
    primary key (usr_id, grp_id)
);
CREATE INDEX usr_grp_1 on usr_grp (grp_id);
CREATE INDEX usr_grp_2 on usr_grp (grp_id, usr_id);
CREATE INDEX usr_grp_3 on usr_grp (usr_id,grp_id);

CREATE TABLE grp_role ( 
    grp_id integer not null default 0, 
    role_id integer not null default 0,
    primary key (grp_id, role_id)
);
CREATE INDEX grp_role_1 on grp_role (role_id);
CREATE INDEX grp_role_2 on grp_role (role_id, grp_id);
CREATE INDEX grp_role_3 on grp_role (grp_id,role_id);

