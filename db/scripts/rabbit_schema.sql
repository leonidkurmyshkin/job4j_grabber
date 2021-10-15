CREATE SCHEMA IF NOT EXISTS job4j;
CREATE TABLE IF NOT EXISTS job4j.rabbit(
	id serial PRIMARY KEY,
    created_date timestamp
);