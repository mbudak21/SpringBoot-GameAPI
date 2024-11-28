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
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    coins INT DEFAULT 5000 NOT NULL CHECK (coins >= 0),
    level INT DEFAULT 1 NOT NULL CHECK (level >= 1),
    country_code CHAR(3) NOT NULL,
    FOREIGN KEY (country_code) REFERENCES countries(code),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 2 users from each country,
INSERT INTO users (country_code) VALUES
    ('TR'),
    ('UK'),
    ('US'),
    ('FRA'),
    ('GER'),
    ('TR'),
    ('UK'),
    ('US'),
    ('FRA'),
    ('GER');

# CREATE TABLE if not exists tournaments (
#     id INT AUTO_INCREMENT PRIMARY KEY,
#     start_time DATETIME NOT NULL,
#     end_time DATETIME NOT NULL,
#     is_active BOOLEAN DEFAULT TRUE NOT NULL,
#     description VARCHAR(500)
# );
#
# -- 1-10 relationship between users and tournaments
# CREATE TABLE if not exists tournament_participants (
#     tournament_id INT NOT NULL,
#     user_id INT NOT NULL,
#     score INT DEFAULT 0 NOT NULL, -- CHECK (score >= 0), omitting this because it could make sense to punish user score
#     team INT DEFAULT 0 NOT NULL, -- Not turning this into a boolean because more teams could be used in the future
#     reward_claimed  BOOLEAN DEFAULT FALSE NOT NULL,
#     FOREIGN KEY (tournament_id) REFERENCES tournaments(id),
#     FOREIGN KEY (user_id) REFERENCES users(id),
#     UNIQUE (tournament_id, user_id),
#     PRIMARY KEY (tournament_id, user_id)
# );
