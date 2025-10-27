
# bankApp

## Project Overview

This is a **Core Banking Management Application** developed in Java using **Swing** for a modern, aesthetically pleasing, dark-themed graphical user interface (GUI). The system implements full CRUD (Create, Read, Update, Delete) operations across five core banking entities: **Customers**, **Accounts**, **Transactions**, **Loans**, and **Loan Payments**.

The architecture follows a clear separation of concerns (Layered Architecture): **UI ➡️ Service ➡️ DAO ➡️ Database**, ensuring the application is robust, maintainable, and scalable.

## 🧾 Table of Contents

- [Features](#features)  
- [Architecture & Design](#architecture--design)  
- [Getting Started](#getting-started)
  - [Project Structure](#Project-Structure)  
  - [SetupandInstallation](#Setup-and-Installation)  
- [Database Design](#database-design)  
- [Usage](#usage)  
- [Contributing](#contributing)  
- [License](#license)

## Features

  * **Customer Management (CRUD):** Complete record management for bank customers, including contact and identification details (PAN).
  * **Account Operations:**
      * Open and close accounts (requires zero balance for closing).
      * **Real-time Deposit & Withdrawal** functionality.
      * Automatic balance updates and transaction logging.
  * **Transaction History:** Dedicated panel to view all transactions, with filtering by Account ID.
  * **Loan Management:** Sanctioning new loans and closing paid loans (requires zero balance).
  * **Loan Payment Tracking:** Record payments against a loan, automatically reducing the outstanding balance, tracked in a separate panel.
  * **Admin Authentication:** Simple login dialog protects access to the system.


## Architecture & Design

| Layer | Technology | Description |
| :--- | :--- | :--- |
| **Frontend/UI** | **Java Swing** | Used for creating the cross-platform desktop GUI with custom L\&F and dark theme components. |
| **Backend/Logic** | **Java 17+ (OOP)** | Implements the Service Layer (Business Logic) and DAO Layer (Data Access). |
| **Data Access** | **JDBC** | Used for direct communication with the relational database. |
| **Database** | **SQL (e.g., MySQL/PostgreSQL)** | Required for persistent data storage (tables: `customers`, `accounts`, `transactions`, `loans`, `loan_payments`). |


- Database Schema: See `dbdesign.pdf` for entity relationships and table definitions.  
- Typical flow: User initiates operation → validation → DB transaction → update account(s) → confirmation.

  

## Getting Started

  ## 🛠 Project Structure

The project maintains a clear separation between data, business logic, and presentation:

```
BankingManagementSystem/
├── src/
│   ├── auth/
│   │   └── AdminAuth.java               # Authentication Utility
│   │
│   ├── db/
│   │   └── DBConnection.java            # JDBC Connection Setup (User implementation required)
│   │
│   ├── model/                           # Data Transfer Objects (DTOs)
│   │   ├── Account.java
│   │   ├── Customer.java
│   │   ├── Loan.java
│   │   ├── LoanPayment.java
│   │   └── Transaction.java
│   │
│   ├── dao/                             # Data Access Objects (CRUD operations)
│   │   ├── AccountDAO.java
│   │   ├── CustomerDAO.java
│   │   ├── LoanDAO.java
│   │   ├── LoanPaymentDAO.java
│   │   └── TransactionDAO.java
│   │
│   ├── service/                         # Business Logic Layer
│   │   ├── AccountService.java
│   │   ├── CustomerService.java
│   │   ├── LoanService.java
│   │   ├── LoanPaymentService.java
│   │   └── TransactionService.java
│   │
│   └── ui/                              # Presentation Layer (Swing GUI)
│       ├── BankingAppUI.java            # Main application frame & initial setup
│       ├── LoginDialog.java             # Admin login dialog
│       ├── CustomerPanel.java           # Customer management tab
│       ├── AccountPanel.java            # Account management tab (Deposit/Withdraw)
│       ├── TransactionPanel.java        # Transaction history tab (View/Filter)
│       ├── LoanPanel.java               # Loan sanctioning/closing tab
│       └── LoanPaymentPanel.java        # Loan payment recording tab
│
└── README.md                            # Project documentation
```


## Setup-and-Installation

### Prerequisites

1.  **Java Development Kit (JDK) 17 or higher.**
2.  An installed SQL Database (e.g., MySQL, PostgreSQL).
3.  The appropriate **JDBC Driver** (JAR file) for your chosen database.

### 1\. Database Setup

You must first create the required tables in your database.

```sql
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



```

### 2\. Configure `db.DBConnection`

Ensure your `db.DBConnection.java` file contains the correct URL, username, and password for your database instance.

### 3\. Run the Application

Execute the main class: `ui.BankingAppUI`.

```bash
# Example command (adjust classpath for your environment)
javac -cp ".:/path/to/jdbc_driver.jar" $(find . -name "*.java")
java -cp ".:/path/to/jdbc_driver.jar" ui.BankingAppUI
```

**Admin Login Credentials:**

  * **Username:** `admin`
  * **Password:** `admin`


## Database Design

See the included `dbdesign.pdf` file for a comprehensive schema diagram.
Major entities:

* **Account**: stores account details (account number, owner name, balance, etc.)
* **Transaction**: logs deposits, withdrawals, and transfers with timestamps
* **Customer** (optional/extendable): if you model customers separately in future



## Contributing

Contributions are very welcome! If you’d like to help:

1. Fork this repository.
2. Create your feature branch: `git checkout -b feature/YourFeatureName`
3. Commit your changes: `git commit -m "Add SomeFeature"`
4. Push to the branch: `git push origin feature/YourFeatureName`
5. Open a Pull Request.
   Please ensure your code is well-documented and includes tests where relevant.

## License

This project is released under the **MIT License** (or choose another license, if you prefer).
See the `LICENSE` file for details.


Thank you for checking out **bankApp**!
Feel free to open issues or send pull requests — let’s build something great together.

```

