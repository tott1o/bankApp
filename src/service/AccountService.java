package service;

import dao.AccountDAO;
import dao.TransactionDAO;
import model.Account;
import model.Transaction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class AccountService {
    private final AccountDAO accountDAO;
    private final TransactionDAO transactionDAO;

    public AccountService(AccountDAO accountDAO, TransactionDAO transactionDAO) {
        this.accountDAO = accountDAO;
        this.transactionDAO = transactionDAO;
    }

    // =========================
    // CRUD Operations (Account)
    // =========================

    public boolean createAccount(Account account) {
        if (account.getBalance() < 0) {
            System.err.println("Initial balance cannot be negative.");
            return false;
        }
        if (account.getOpenDate() == null) {
            account.setOpenDate(LocalDate.now());
        }
        return accountDAO.addAccount(account);
    }

    public Account getAccount(int id) {
        return accountDAO.getAccountById(id);
    }

    public List<Account> getAllAccounts() {
        return accountDAO.getAllAccounts();
    }

    public boolean updateAccount(Account account) {
        if (account.getId() <= 0) {
            System.err.println("Cannot update account: ID is missing.");
            return false;
        }
        // Assuming only fields like account_type or close_date are updated through the standard update.
        return accountDAO.updateAccount(account);
    }

    public boolean deleteAccount(int id) {
        Account account = accountDAO.getAccountById(id);

        if (account == null) {
            System.err.println("Account not found for deletion.");
            return false;
        }

        // Business Logic Check: Cannot delete if balance is not zero.
        if (account.getBalance() != 0.0) {
            System.err.println("Cannot delete account ID " + id + ". Balance must be zero.");
            return false;
        }

        return accountDAO.deleteAccount(id);
    }

    // =========================
    // Core Business Transactions
    // =========================

    public boolean withdraw(int accountId, double amount) {
        if (amount <= 0) return false;

        Account account = accountDAO.getAccountById(accountId);
        if (account == null) {
            System.err.println("Account not found for withdrawal.");
            return false;
        }

        // **Critical Business Logic Check: Insufficient Funds**
        if (account.getBalance() < amount) {
            System.err.println("Withdrawal failed: Insufficient funds in account ID " + accountId);
            return false;
        }

        // 1. Update account balance
        account.setBalance(account.getBalance() - amount);
        if (accountDAO.updateAccount(account)) {
            // 2. Record transaction
            Transaction transaction = new Transaction();
            transaction.setAccountId(accountId);
            transaction.setAmount(amount);
            transaction.setTransactionType(Transaction.TransactionType.WITHDRAWAL);
            transaction.setDate(LocalDateTime.now());
            transaction.setNarration("Cash Withdrawal");

            return transactionDAO.addTransaction(transaction);
        }
        return false;
    }

    public boolean deposit(int accountId, double amount) {
        if (amount <= 0) {
            System.err.println("Deposit amount must be positive.");
            return false;
        }

        Account account = accountDAO.getAccountById(accountId);
        if (account == null) {
            System.err.println("Account not found for deposit.");
            return false;
        }

        // 1. Update account balance
        account.setBalance(account.getBalance() + amount);

        if (accountDAO.updateAccount(account)) {
            // 2. Record transaction
            Transaction transaction = new Transaction();
            transaction.setAccountId(accountId);
            transaction.setAmount(amount);
            transaction.setTransactionType(Transaction.TransactionType.DEPOSIT);
            transaction.setDate(LocalDateTime.now());
            transaction.setNarration("Cash Deposit");

            // Atomicity check: both must succeed
            return transactionDAO.addTransaction(transaction);
        }
        return false;
    }

    /**
     * Apply interest for a single account. Interest is taken from the account's interestRate (percentage).
     * Creates a DEPOSIT transaction with narration 'Interest applied'.
     */
    public boolean applyInterest(int accountId) {
        Account account = accountDAO.getAccountById(accountId);
        if (account == null) {
            System.err.println("Account not found for applying interest.");
            return false;
        }

        double rate = account.getInterestRate();
        if (rate <= 0) {
            System.err.println("Interest rate is not set or zero for account ID " + accountId);
            return false;
        }

        double interest = account.getBalance() * rate / 100.0; // simple interest for the balance snapshot
        if (interest <= 0.0) {
            System.err.println("Calculated interest is zero for account ID " + accountId);
            return false;
        }

        // 1. Update account balance
        account.setBalance(account.getBalance() + interest);
        if (accountDAO.updateAccount(account)) {
            // 2. Record interest transaction as a deposit
            Transaction transaction = new Transaction();
            transaction.setAccountId(accountId);
            transaction.setAmount(interest);
            transaction.setTransactionType(Transaction.TransactionType.DEPOSIT);
            transaction.setDate(LocalDateTime.now());
            transaction.setNarration("Interest applied (" + rate + "%)");

            return transactionDAO.addTransaction(transaction);
        }
        return false;
    }

    /**
     * Apply interest to all accounts. Returns number of accounts successfully credited.
     */
    public int applyInterestToAll() {
        int successCount = 0;
        List<Account> accounts = accountDAO.getAllAccounts();
        for (Account acc : accounts) {
            try {
                if (applyInterest(acc.getId())) successCount++;
            } catch (Exception ex) {
                // continue to next account
                ex.printStackTrace();
            }
        }
        return successCount;
    }
}