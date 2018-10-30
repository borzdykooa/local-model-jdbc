
DROP DATABASE test_database;

CREATE DATABASE test_database DEFAULT CHARACTER SET utf8;
USE test_database;

CREATE TABLE trainer (
  id         BIGINT PRIMARY KEY AUTO_INCREMENT,
  name       VARCHAR(32),
  language   VARCHAR(16) NOT NULL,
  experience INTEGER     NOT NULL
);

CREATE TABLE trainee (
  id         BIGINT PRIMARY KEY AUTO_INCREMENT,
  name       VARCHAR(32) NOT NULL,
  trainer_id BIGINT      REFERENCES test_database.trainer(id)
);
