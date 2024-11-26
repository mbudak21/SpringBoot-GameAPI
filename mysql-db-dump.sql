CREATE TABLE if not exists users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       coins INT DEFAULT 5000 NOT NULL,
                       level INT DEFAULT 1 NOT NULL,
                       country VARCHAR(50) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

# INSERT INTO users (coins, level, country)
# VALUES
#     (5000, 1, 'United States'),
#     (5000, 1, 'United Kingdom'),
#     (5000, 1, 'France'),
#     (5000, 1, 'Germany');
