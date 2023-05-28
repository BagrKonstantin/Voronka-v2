CREATE TABLE users
(
    user_id       INT AUTO_INCREMENT PRIMARY KEY,
    username      VARCHAR(50) UNIQUE  NOT NULL,
    email         VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255)        NOT NULL,
    role          VARCHAR(10)         NOT NULL CHECK (role IN ('customer', 'chef', 'manager', 'admin')),
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP()
);
