package test;

import dao.*;
import model.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TestApp {
    public static void main(String[] args) {
        System.out.println("=== BANK SYSTEM TEST START ===");

        // --- Initialize DAOs ---
        CustomerDAO customerDAO = new CustomerDAO();
        AccountDAO accountDAO = new AccountDAO();
        TransactionDAO transactionDAO = new TransactionDAO();
        LoanDAO loanDAO = new LoanDAO();

        // -------------------------------
        // 1. TEST CUSTOMER
        // -------------------------------
        System.out.println("\n[1] Testing CustomerDAO...");
        Customer c = new Customer();
        c.setFullName("Test User");
        c.setAddress("123 Main Street");
        c.setContactNo("9876543210");
        c.setEmail("testuser@example.com");
        c.setPanNumber("ABCDE1234F");
        c.setCreatedAt(LocalDateTime.now());

        boolean customerAdded = customerDAO.addCustomer(c);
        System.out.println("Customer Inserted: " + customerAdded);

        Customer fetchedCustomer = customerDAO.getCustomerById(1); // Assuming ID=1 or adjust
        if (fetchedCustomer != null) {
            System.out.println("Fetched Customer: " + fetchedCustomer.getFullName());
        } else {
            System.out.println("No customer found in DB!");
        }

        // -------------------------------
        // 2. TEST ACCOUNT
        // -------------------------------
        System.out.println("\n[2] Testing AccountDAO...");
        Account a = new Account();
        a.setCustomerId(1); // change based on your DB
        a.setAccountType(Account.AccountType.SAVINGS);
        a.setBalance(5000.00);
        a.setOpenDate(LocalDate.now());

        boolean accountAdded = accountDAO.addAccount(a);
        System.out.println("Account Inserted: " + accountAdded);

        Account fetchedAccount = accountDAO.getAccountById(1);
        if (fetchedAccount != null) {
            System.out.println("Fetched Account Type: " + fetchedAccount.getAccountType());
            System.out.println("Balance: " + fetchedAccount.getBalance());
        } else {
            System.out.println("No account found in DB!");
        }

        // -------------------------------
        // 3. TEST TRANSACTION
        // -------------------------------
        System.out.println("\n[3] Testing TransactionDAO...");
        Transaction t = new Transaction();
        t.setAccountId(1);
        t.setTransactionType(Transaction.TransactionType.DEPOSIT);
        t.setAmount(1500.00);
        t.setNarration("Initial deposit");
        t.setTransactionDate(LocalDateTime.now());

        boolean transactionAdded = transactionDAO.addTransaction(t);
        System.out.println("Transaction Inserted: " + transactionAdded);

        Transaction fetchedTransaction = transactionDAO.getTransactionById(1);
        if (fetchedTransaction != null) {
            System.out.println("Fetched Transaction: " + fetchedTransaction.getTransactionType() +
                    " ₹" + fetchedTransaction.getAmount());
        } else {
            System.out.println("No transaction found in DB!");
        }

        // -------------------------------
        // 4. TEST LOAN (OPTIONAL)
        // -------------------------------
        System.out.println("\n[4] Testing LoanDAO...");
        Loan loan = new Loan();
        loan.setCustomerId(1);
        loan.setLoanType(Loan.LoanType.PERSONAL_LOAN);
        loan.setAmountSanctioned(25000.00);
        loan.setOpenDate(LocalDate.now());
        loan.setBalance(25000.00);

        boolean loanAdded = loanDAO.addLoan(loan);
        System.out.println("Loan Inserted: " + loanAdded);

        Loan fetchedLoan = loanDAO.getLoanById(1);
        if (fetchedLoan != null) {
            System.out.println("Fetched Loan Type: " + fetchedLoan.getLoanType());
            System.out.println("Remaining Balance: ₹" + fetchedLoan.getBalance());
        } else {
            System.out.println("No loan found in DB!");
        }

        // -------------------------------
        // FINISH
        // -------------------------------
        System.out.println("\n=== TEST COMPLETE ===");
    }
}

