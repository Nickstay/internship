CREATE TABLE department (
    id bigserial PRIMARY KEY,
    name varchar(40) UNIQUE NOT NULL
);

CREATE TABLE worker (
    id bigserial PRIMARY KEY,
    first_name varchar(20) NOT NULL,
    last_name varchar(20) NOT NULL,
    phone_number varchar(15),
    department_id bigint NOT NULL CONSTRAINT department_id_fk REFERENCES department(id) ON DELETE RESTRICT
);

