CREATE DATABASE IF NOT EXISTS bank2;
USE bank2;

CREATE TABLE IF NOT EXISTS customers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    contact_no VARCHAR(20),
    email VARCHAR(100),
    pan_number VARCHAR(20),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS accounts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    account_type ENUM('SAVINGS', 'CURRENT', 'FIXED_DEPOSIT', 'RECURRING_DEPOSIT') NOT NULL,
    open_date DATE NOT NULL,
    close_date DATE,
    balance DECIMAL(15,2) DEFAULT 0.0,
    interest_rate DECIMAL(5,2) DEFAULT 0.0,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    account_id INT NOT NULL,
    transaction_type ENUM('DEPOSIT', 'WITHDRAWAL') NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    date DATETIME DEFAULT CURRENT_TIMESTAMP,
    narration VARCHAR(255),
    FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS loans (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    loan_type ENUM('PERSONAL_LOAN','HOME_LOAN','VEHICLE_LOAN','GOLD_LOAN') NOT NULL,
    amount_sanctioned DECIMAL(15,2) NOT NULL,
    balance DECIMAL(15,2) NOT NULL,
    interest_rate DECIMAL(5,2) DEFAULT 0.0,
    open_date DATE NOT NULL,
    close_date DATE,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS loan_payments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    loan_id INT NOT NULL,
    disbursement_amount DECIMAL(15,2) NOT NULL,
    receipt_no VARCHAR(50),
    payment_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    remaining_balance DECIMAL(15,2) NOT NULL,
    FOREIGN KEY (loan_id) REFERENCES loans(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS transfers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sender_account_number INT NOT NULL,
    receiver_account_number INT,
    receiver_bank_name VARCHAR(100),
    ifsc_code VARCHAR(20),
    amount DECIMAL(15,2) NOT NULL,
    mode ENUM('NEFT','RTGS') NOT NULL,
    date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_account_number) REFERENCES accounts(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS cheques (
    id INT AUTO_INCREMENT PRIMARY KEY,
    account_id INT NOT NULL,
    cheque_number VARCHAR(50) UNIQUE NOT NULL,
    issue_date DATE NOT NULL,
    payee_name VARCHAR(100),
    amount DECIMAL(15,2) NOT NULL,
    status ENUM('ISSUED','CLEARED','BOUNCED','CANCELLED') NOT NULL,
    cleared_date DATETIME,
    FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE
);
