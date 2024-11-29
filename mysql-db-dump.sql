DROP DATABASE `mysql-db`;
CREATE DATABASE `mysql-db`;
USE `mysql-db`;

CREATE TABLE IF NOT EXISTS countries (
    code CHAR(3) PRIMARY KEY, -- ISO 3166-1 alpha-3 standard
    name VARCHAR(100) NOT NULL
);
-- Delete any previous element, ideally there shouldn't be any
-- Make sure the default country values are inserted
INSERT IGNORE INTO countries VALUES
    ('TR', 'Turkey'),
    ('US', 'United States'),
    ('UK', 'United Kingdom'),
    ('FRA', 'France'),
    ('GER', 'Germany');


CREATE TABLE if not exists users (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(20) NOT NULL,
    UNIQUE(username),
    coins INT DEFAULT 5000 NOT NULL CHECK (coins >= 0),
    level INT DEFAULT 1 NOT NULL CHECK (level >= 1),
    country_code CHAR(3) NOT NULL,
    FOREIGN KEY (country_code) REFERENCES countries(code),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 2 users from each country,
INSERT INTO users (username, country_code) VALUES
    ('t1', 'TR'),
    ('t2', 'TR'),
    ('t3', 'TR'),
    ('ahmet', 'TR'),
    ('john', 'UK'),
    ('mike', 'US'),
    ('pierre', 'FRA'),
    ('hans', 'GER'),
    ('mehmet', 'TR'),
    ('george', 'UK'),
    ('kevin', 'US'),
    ('luc', 'FRA'),
    ('karl', 'GER');


CREATE TABLE if not exists tournaments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    description VARCHAR(500)
);

INSERT INTO tournaments (tournaments.start_time, tournaments.end_time, tournaments.description) VALUES
    ('2000-11-30 10:00:00', '2000-12-01 18:00:00', 'cool tournament1'),
    ('2024-11-29 00:00:00', '2024-11-29 20:00:00', 'cool active tournament');

CREATE TABLE IF NOT EXISTS tournament_bracket ( -- Instance of a tournament
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    tournament_id BIGINT,
    max_teams INT DEFAULT 2,
    participants_per_team INT DEFAULT 5,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (tournament_id) REFERENCES tournaments(id)
);

-- 1-10 relationship between users and tournaments
CREATE TABLE IF NOT EXISTS tournament_participants (
    tournament_bracket_id BIGINT UNSIGNED NOT NULL,
    user_id BIGINT UNSIGNED NOT NULL, -- Match the type of `id` in `users`
    score INT DEFAULT 0 NOT NULL,
    team INT DEFAULT 0 NOT NULL,
    reward_claimed BOOLEAN DEFAULT FALSE NOT NULL,
    FOREIGN KEY (tournament_bracket_id) REFERENCES tournament_bracket(id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    UNIQUE (tournament_bracket_id, user_id),
    PRIMARY KEY (tournament_bracket_id, user_id)
);

