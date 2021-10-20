CREATE SCHEMA IF NOT EXISTS job4j;
CREATE TABLE IF NOT EXISTS job4j.post(
	id serial PRIMARY KEY,
	name VARCHAR(200),
	text TEXT,
	link VARCHAR(200),
    created timestamp UNIQUE
);