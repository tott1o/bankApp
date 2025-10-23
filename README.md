
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
src/
├── auth/
│   └── AdminAuth.java     # Simple hardcoded authentication
├── db/
│   └── DBConnection.java  # JDBC connection setup
├── model/
│   ├── Account.java       # Data objects (DTOs)
│   ├── Customer.java
│   └── ...
├── dao/                   # Data Access Objects (CRUD/SQL queries)
│   ├── AccountDAO.java
│   ├── CustomerDAO.java
│   └── ...
├── service/               # Business Logic Layer (Deposit/Withdraw checks)
│   ├── AccountService.java
│   ├── CustomerService.java
│   └── ...
└── ui/                    # Presentation Layer (Swing GUI)
    ├── BankingAppUI.java  # Main application frame and setup
    ├── LoginDialog.java   # Admin login screen
    ├── CustomerPanel.java # Customer tab content
    ├── AccountPanel.java  # Account tab content (Deposit/Withdraw UI)
    └── ...
```


## Setup-and-Installation

### Prerequisites

1.  **Java Development Kit (JDK) 17 or higher.**
2.  An installed SQL Database (e.g., MySQL, PostgreSQL).
3.  The appropriate **JDBC Driver** (JAR file) for your chosen database.

### 1\. Database Setup

You must first create the required tables in your database.

```sql


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

