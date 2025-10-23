
# bankApp

A simple banking-application built in Java.  
This project showcases core banking operations (such as account creation, deposits, withdrawals, transfers) along with a database design for persistence.

## 🧾 Table of Contents

- [Features](#features)  
- [Architecture & Design](#architecture--design)  
- [Getting Started](#getting-started)  
  - [Prerequisites](#prerequisites)  
  - [Installation](#installation)  
  - [Running the Application](#running-the-application)  
- [Database Design](#database-design)  
- [Usage](#usage)  
- [Contributing](#contributing)  
- [License](#license)

## Features

- Create bank accounts  
- Deposit and withdraw funds  
- Transfer money between accounts  
- Persisted data using a relational database  
- Simple, clear code structure (Java)  
- Designed to be extended (e.g., with UI, REST API, security)  

## Architecture & Design

- Language: **Java**  
- Folder structure:  
```

.idea/
src/  → application source code
.gitignore
dbdesign.pdf  → database schema diagram
bankApp.iml  → IDE module file

````
- Database Schema: See `dbdesign.pdf` for entity relationships and table definitions.  
- Typical flow: User initiates operation → validation → DB transaction → update account(s) → confirmation.

## Getting Started

### Prerequisites

- Java JDK (version 11 or above recommended)  
- A relational database (e.g., MySQL, PostgreSQL, H2)  
- (Optional) An IDE such as IntelliJ IDEA, Eclipse  

### Installation

1. Clone this repository  
 ```bash
 git clone https://github.com/tott1o/bankApp.git
 cd bankApp
````

2. Configure your database settings in the application configuration (e.g., connection URL, user, password).
3. Build the project (if using Maven/Gradle) or compile via your IDE.

### Running the Application

* Run the `main()` method in the appropriate class (e.g., `Application.java`) via your IDE or from command-line.
* Follow console prompts (or UI if added) to perform banking operations.
* Verify that transactions are persisted to the configured database.

## Database Design

See the included `dbdesign.pdf` file for a comprehensive schema diagram.
Major entities:

* **Account**: stores account details (account number, owner name, balance, etc.)
* **Transaction**: logs deposits, withdrawals, and transfers with timestamps
* **Customer** (optional/extendable): if you model customers separately in future

## Usage

Here are typical interactions:

1. Create a new account with initial deposit.
2. Deposit funds into an existing account.
3. Withdraw funds, with checks for sufficient balance.
4. Transfer money between two accounts (source → destination).
5. Query account details / transaction history (if implemented).

**Example**:

```
Welcome to bankApp!
Please select an option:
1) Create Account
2) Deposit
3) Withdraw
4) Transfer
5) Exit
```

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

---

Thank you for checking out **bankApp**!
Feel free to open issues or send pull requests — let’s build something great together. 🚀

```

