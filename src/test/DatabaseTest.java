package test;

import dao.*;
import model.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class DatabaseTest {
    public static void main(String[] args) {

        CustomerDAO customerDAO = new CustomerDAO();
        AccountDAO accountDAO = new AccountDAO();
        TransactionDAO transactionDAO = new TransactionDAO();
        LoanDAO loanDAO = new LoanDAO();
        LoanPaymentDAO loanPaymentDAO = new LoanPaymentDAO();

        // =============================
        // 1️⃣ TEST CUSTOMER INSERTION
        // =============================
        Customer customer = new Customer();
        customer.setFullName("Akhil Nath");
        customer.setAddress("Kasaragod, Kerala");
        customer.setContactNo("9876543210");
        customer.setEmail("akhil@example.com");
        customer.setPanNumber("ABCDE1234F");
        customer.setCreatedAt(LocalDateTime.now());

        boolean customerInserted = customerDAO.addCustomer(customer);
        System.out.println("✅ Customer inserted: " + customerInserted);

        // =============================
        // 2️⃣ FETCH CUSTOMERS
        // =============================
        List<Customer> customers = customerDAO.getAllCustomers();
        System.out.println("📋 All Customers:");
        for (Customer c : customers) {
            System.out.println(" - " + c.getId() + " | " + c.getFullName());
        }

        // =============================
        // 3️⃣ TEST ACCOUNT INSERTION
        // =============================
        if (!customers.isEmpty()) {
            int customerId = customers.get(0).getId();

            Account acc = new Account();
            acc.setCustomerId(customerId);
            acc.setAccountType(Account.AccountType.SAVINGS);
            acc.setOpenDate(LocalDate.now());
            acc.setBalance(5000.00);

            boolean accountInserted = accountDAO.addAccount(acc);
            System.out.println("✅ Account inserted: " + accountInserted);
        }

        // =============================
        // 4️⃣ FETCH ACCOUNTS
        // =============================
        List<Account> accounts = accountDAO.getAllAccounts();
        System.out.println("📋 All Accounts:");
        for (Account a : accounts) {
            System.out.println(" - Account ID: " + a.getId() +
                    ", Customer ID: " + a.getCustomerId() +
                    ", Type: " + a.getAccountType() +
                    ", Balance: " + a.getBalance());
        }

        // =============================
        // 5️⃣ TEST TRANSACTION INSERTION
        // =============================
        if (!accounts.isEmpty()) {
            int accountId = accounts.get(0).getId();

            Transaction txn = new Transaction();
            txn.setAccountId(accountId);
            txn.setTransactionType(Transaction.TransactionType.DEPOSIT);
            txn.setAmount(1500.00);
            txn.setDate(LocalDateTime.now());
            txn.setNarration("Initial Deposit");

            boolean transactionInserted = transactionDAO.addTransaction(txn);
            System.out.println("✅ Transaction inserted: " + transactionInserted);
        }

        // =============================
        // 6️⃣ FETCH TRANSACTIONS
        // =============================
        List<Transaction> transactions = transactionDAO.getAllTransactions();
        System.out.println("📋 All Transactions:");
        for (Transaction t : transactions) {
            System.out.println(" - Txn ID: " + t.getId() +
                    ", Account ID: " + t.getAccountId() +
                    ", Type: " + t.getTransactionType() +
                    ", Amount: " + t.getAmount());
        }

        // =============================
        // 7️⃣ TEST LOAN + PAYMENT INSERTION
        // =============================
        if (!customers.isEmpty()) {
            int customerId = customers.get(0).getId();

            Loan loan = new Loan();
            loan.setCustomerId(customerId);
            loan.setLoanType(Loan.LoanType.PERSONAL_LOAN);
            loan.setAmountSanctioned(10000.00);
            loan.setBalance(10000.00);
            loan.setOpenDate(LocalDate.now());

            boolean loanInserted = loanDAO.addLoan(loan);
            System.out.println("✅ Loan inserted: " + loanInserted);

            if (loanInserted) {
                LoanPayment payment = new LoanPayment();
                payment.setLoanId(loan.getId());
                payment.setDisbursementAmount(2000.00);
                payment.setReceiptNo("RCPT001");
                payment.setPaymentDate(LocalDateTime.now());
                payment.setRemainingBalance(8000.00);

                boolean paymentInserted = loanPaymentDAO.addLoanPayment(payment);
                System.out.println("✅ Loan Payment inserted: " + paymentInserted);
            }
        }

        // =============================
        // 8️⃣ FETCH LOANS
        // =============================
        List<Loan> loans = loanDAO.getAllLoans();
        System.out.println("📋 All Loans:");
        for (Loan l : loans) {
            System.out.println(" - Loan ID: " + l.getId() +
                    ", Customer ID: " + l.getCustomerId() +
                    ", Type: " + l.getLoanType() +
                    ", Balance: " + l.getBalance());
        }
    }
}
