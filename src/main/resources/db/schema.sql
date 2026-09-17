CREATE DATABASE IF NOT EXISTS student_budget_db;
USE student_budget_db;

CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE monthly_budget (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    month VARCHAR(7) NOT NULL,
    budget DECIMAL(12,2) NOT NULL,
    user_id BIGINT NOT NULL,
    UNIQUE KEY unique_user_month (user_id, month),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE expense (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    amount DECIMAL(12,2) NOT NULL,
    category VARCHAR(30) NOT NULL,
    date DATE NOT NULL,
    note VARCHAR(250),
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE recurring_expense (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    category VARCHAR(30) NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
