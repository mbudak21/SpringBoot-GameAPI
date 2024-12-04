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

INSERT INTO tournaments (tournaments.start_time, tournaments.end_time, tournaments.description) VALUES
('2000-11-30 10:00:00', '2000-12-01 18:00:00', 'cool tournament1'),
('2024-11-29 00:00:00', '2024-11-29 20:00:00', 'cool active tournament'),
('2024-11-29 00:00:00', '2029-11-29 20:00:00', 'always active tournament');

INSERT INTO tournament_bracket (tournament_id) VALUES
(1),
(2),
(2),
(3);

INSERT INTO tournament_participants (tournament_bracket_id, user_id, team) VALUES
(1, 1, 0),
(1, 2, 1),
(1, 3, 0),

(4, 4, 0),
(4, 5, 0),
(4, 6, 0),
(4, 7, 0),
(4, 8, 0),
(4, 9, 1),
(4, 10, 1),
(4, 11, 1),
(4, 12, 1),
(4, 13, 1);